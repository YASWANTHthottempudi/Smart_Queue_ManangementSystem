package com.example.queue.service;

import com.example.queue.dto.CounterResponse;
import com.example.queue.model.Counter;
import com.example.queue.model.Token;
import com.example.queue.repository.CounterRepository;
import com.example.queue.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CounterService {
    
    private final CounterRepository counterRepository;
    private final TokenRepository tokenRepository;
    
    public List<CounterResponse> getAllCounters() {
        List<Counter> counters = counterRepository.findAll();
        return counters.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    public List<CounterResponse> getCountersByQueue(Long queueId) {
        List<Counter> counters = counterRepository.findByQueueId(queueId);
        return counters.stream()
            .map(this::convertToResponse)
            .collect(Collectors.toList());
    }
    
    @Transactional
    public Token serveNextToken(Long counterId) {
        Counter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));
        
        if (counter.getStatus() != Counter.CounterStatus.AVAILABLE) {
            throw new RuntimeException("Counter is not available");
        }
        
        // Find next waiting token for this queue
        List<Token> waitingTokens = tokenRepository.findByQueueIdOrderByTokenNumberAsc(counter.getQueue().getId());
        Token nextToken = waitingTokens.stream()
            .filter(t -> t.getStatus() == Token.TokenStatus.WAITING)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("No waiting tokens"));
        
        // Update token
        nextToken.setStatus(Token.TokenStatus.SERVING);
        nextToken.setCounterId(counterId);
        tokenRepository.save(nextToken);
        
        // Update counter
        counter.setStatus(Counter.CounterStatus.BUSY);
        counter.setCurrentTokenId(nextToken.getId());
        counterRepository.save(counter);
        
        return nextToken;
    }
    
    @Transactional
    public void completeToken(Long counterId, Long tokenId) {
        Counter counter = counterRepository.findById(counterId)
            .orElseThrow(() -> new RuntimeException("Counter not found"));
        
        Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        
        // Update token
        token.setStatus(Token.TokenStatus.SERVED);
        token.setServedTime(LocalDateTime.now());
        tokenRepository.save(token);
        
        // Update counter
        counter.setStatus(Counter.CounterStatus.AVAILABLE);
        counter.setCurrentTokenId(null);
        counterRepository.save(counter);
    }
    
    private CounterResponse convertToResponse(Counter counter) {
        Integer currentTokenNumber = null;
        if (counter.getCurrentTokenId() != null) {
            Token token = tokenRepository.findById(counter.getCurrentTokenId()).orElse(null);
            if (token != null) {
                currentTokenNumber = token.getTokenNumber();
            }
        }
        
        return new CounterResponse(
            counter.getId(),
            counter.getCounterNumber(),
            counter.getQueue().getId(),
            counter.getStatus().name(),
            counter.getCurrentTokenId(),
            currentTokenNumber
        );
    }
}
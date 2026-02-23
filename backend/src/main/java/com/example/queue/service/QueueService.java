package com.example.queue.service;

import com.example.queue.dto.QueueResponse;
import com.example.queue.dto.QueueStatusResponse;
import com.example.queue.model.Queue;
import com.example.queue.model.Token;
import com.example.queue.repository.QueueRepository;
import com.example.queue.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QueueService {
    
    private final TokenRepository tokenRepository;
    private final QueueRepository queueRepository;

    public List<QueueResponse> getAllQueues() {
        return queueRepository.findAll().stream()
                .map(this::toQueueResponse)
                .collect(Collectors.toList());
    }

    private QueueResponse toQueueResponse(Queue queue) {
        return new QueueResponse(queue.getId(), queue.getName(), queue.getDepartment());
    }
    
    public QueueStatusResponse getQueueStatus(Long tokenId) {
        Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        
        // Get current serving token
        List<Token> servingTokens = tokenRepository.findByStatus(Token.TokenStatus.SERVING);
        Integer currentServingToken = servingTokens.stream()
            .filter(t -> t.getQueue().getId().equals(token.getQueue().getId()))
            .map(Token::getTokenNumber)
            .findFirst()
            .orElse(null);
        
        // Calculate position
        List<Token> waitingTokens = tokenRepository.findByQueueIdOrderByTokenNumberAsc(token.getQueue().getId());
        int position = waitingTokens.stream()
            .filter(t -> t.getStatus() == Token.TokenStatus.WAITING && t.getTokenNumber() < token.getTokenNumber())
            .mapToInt(t -> 1)
            .sum() + 1;
        
        // Estimate wait time
        Long estimatedWaitTime = (long) (position - 1) * 5;
        
        return new QueueStatusResponse(
            token.getId(),
            token.getTokenNumber(),
            token.getStatus().name(),
            position,
            estimatedWaitTime,
            currentServingToken
        );
    }
    
    public Integer getCurrentServingToken(Long queueId) {
        List<Token> servingTokens = tokenRepository.findByStatus(Token.TokenStatus.SERVING);
        return servingTokens.stream()
            .filter(t -> t.getQueue().getId().equals(queueId))
            .map(Token::getTokenNumber)
            .findFirst()
            .orElse(null);
    }
}
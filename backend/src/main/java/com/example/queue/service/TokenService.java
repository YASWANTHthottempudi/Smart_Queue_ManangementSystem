package com.example.queue.service;

import com.example.queue.dto.TokenRequest;
import com.example.queue.dto.TokenResponse;
import com.example.queue.model.Queue;
import com.example.queue.model.Token;
import com.example.queue.model.User;
import com.example.queue.repository.QueueRepository;
import com.example.queue.repository.TokenRepository;
import com.example.queue.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TokenService {
    
    private final TokenRepository tokenRepository;
    private final QueueRepository queueRepository;
    private final UserRepository userRepository;
    
    @Transactional
    public TokenResponse createToken(TokenRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        Queue queue = queueRepository.findById(request.getQueueId())
            .orElseThrow(() -> new RuntimeException("Queue not found"));
        
        // Get next token number
        Integer maxTokenNumber = tokenRepository.findMaxTokenNumberByQueueId(queue.getId());
        Integer nextTokenNumber = (maxTokenNumber == null) ? 1 : maxTokenNumber + 1;
        
        Token token = new Token();
        token.setTokenNumber(nextTokenNumber);
        token.setQueue(queue);
        token.setUser(user);
        token.setStatus(Token.TokenStatus.WAITING);
        token.setCreatedTime(LocalDateTime.now());
        
        token = tokenRepository.save(token);
        
        // Calculate position in queue
        Long waitingCount = tokenRepository.countWaitingTokensByQueueId(queue.getId());
        int position = waitingCount.intValue();
        
        return convertToResponse(token, position);
    }
    
    public TokenResponse getTokenStatus(Long tokenId, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Token token = tokenRepository.findById(tokenId)
            .orElseThrow(() -> new RuntimeException("Token not found"));
        if (!token.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Token does not belong to this user");
        }
        
        // Calculate position
        List<Token> waitingTokens = tokenRepository.findByQueueIdOrderByTokenNumberAsc(token.getQueue().getId());
        int position = waitingTokens.stream()
            .filter(t -> t.getStatus() == Token.TokenStatus.WAITING && t.getTokenNumber() < token.getTokenNumber())
            .mapToInt(t -> 1)
            .sum() + 1;
        
        return convertToResponse(token, position);
    }
    
    public List<TokenResponse> getUserTokens(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        List<Token> tokens = tokenRepository.findByUserIdOrderByCreatedTimeDesc(user.getId());
        
        return tokens.stream()
            .map(token -> {
                List<Token> waitingTokens = tokenRepository.findByQueueIdOrderByTokenNumberAsc(token.getQueue().getId());
                int position = waitingTokens.stream()
                    .filter(t -> t.getStatus() == Token.TokenStatus.WAITING && t.getTokenNumber() < token.getTokenNumber())
                    .mapToInt(t -> 1)
                    .sum() + 1;
                return convertToResponse(token, position);
            })
            .collect(Collectors.toList());
    }
    
    private TokenResponse convertToResponse(Token token, int position) {
        // Estimate wait time: 5 minutes per token ahead
        Long estimatedWaitTime = (long) (position - 1) * 5;
        
        return new TokenResponse(
            token.getId(),
            token.getTokenNumber(),
            token.getQueue().getId(),
            token.getQueue().getName(),
            token.getStatus().name(),
            token.getCreatedTime(),
            position,
            estimatedWaitTime
        );
    }
}
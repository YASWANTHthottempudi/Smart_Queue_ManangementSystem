package com.example.queue.repository;

import com.example.queue.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    List<Token> findByQueueIdOrderByTokenNumberAsc(Long queueId);
    List<Token> findByUserIdOrderByCreatedTimeDesc(Long userId);
    List<Token> findByStatus(Token.TokenStatus status);
    Optional<Token> findByTokenNumberAndQueueId(Integer tokenNumber, Long queueId);
    
    @Query("SELECT MAX(t.tokenNumber) FROM Token t WHERE t.queue.id = :queueId")
    Integer findMaxTokenNumberByQueueId(Long queueId);
    
    @Query("SELECT COUNT(t) FROM Token t WHERE t.queue.id = :queueId AND t.status = 'WAITING'")
    Long countWaitingTokensByQueueId(Long queueId);
}
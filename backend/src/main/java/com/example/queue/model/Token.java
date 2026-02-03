package com.example.queue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "token_number", nullable = false)
    private Integer tokenNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id", nullable = false)
    private Queue queue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TokenStatus status = TokenStatus.WAITING;
    
    @Column(name = "created_time", nullable = false)
    private LocalDateTime createdTime = LocalDateTime.now();
    
    @Column(name = "served_time")
    private LocalDateTime servedTime;
    
    @Column(name = "counter_id")
    private Long counterId;
    
    public enum TokenStatus {
        WAITING, SERVING, SERVED, CANCELLED
    }
}
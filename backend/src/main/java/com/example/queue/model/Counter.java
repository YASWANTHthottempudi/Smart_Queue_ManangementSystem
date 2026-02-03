package com.example.queue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "counters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Counter {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "counter_number", nullable = false)
    private Integer counterNumber;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "queue_id", nullable = false)
    private Queue queue;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CounterStatus status = CounterStatus.AVAILABLE;
    
    @Column(name = "current_token_id")
    private Long currentTokenId;
    
    public enum CounterStatus {
        AVAILABLE, BUSY, OFFLINE
    }
}
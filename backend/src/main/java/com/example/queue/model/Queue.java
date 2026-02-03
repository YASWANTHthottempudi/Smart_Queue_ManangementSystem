package com.example.queue.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "queues")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Queue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String department;
    
    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL)
    private List<Counter> counters;
    
    @OneToMany(mappedBy = "queue", cascade = CascadeType.ALL)
    private List<Token> tokens;
}
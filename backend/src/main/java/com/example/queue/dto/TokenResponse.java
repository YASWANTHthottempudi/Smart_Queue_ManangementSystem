package com.example.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private Long id;
    private Integer tokenNumber;
    private Long queueId;
    private String queueName;
    private String status;
    private LocalDateTime createdTime;
    private Integer positionInQueue;
    private Long estimatedWaitTimeMinutes;
}
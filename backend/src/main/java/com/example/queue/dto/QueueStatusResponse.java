package com.example.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueStatusResponse {
    private Long tokenId;
    private Integer tokenNumber;
    private String status;
    private Integer positionInQueue;
    private Long estimatedWaitTimeMinutes;
    private Integer currentServingToken;
}
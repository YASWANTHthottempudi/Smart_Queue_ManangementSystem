package com.example.queue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TokenRequest {
    
    @NotNull(message = "Queue ID is required")
    private Long queueId;
}
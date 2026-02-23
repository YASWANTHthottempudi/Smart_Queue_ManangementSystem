package com.example.queue.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CounterRequest {
    @NotNull(message = "Queue ID is required")
    private Long queueId;

    @NotNull(message = "Counter number is required")
    private Integer counterNumber;
}

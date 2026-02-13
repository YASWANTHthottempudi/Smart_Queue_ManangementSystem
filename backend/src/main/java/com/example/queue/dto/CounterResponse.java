package com.example.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CounterResponse {
    private Long id;
    private Integer counterNumber;
    private Long queueId;
    private String status;
    private Long currentTokenId;
    private Integer currentTokenNumber;
}
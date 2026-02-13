package com.example.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsResponse {
    private Long totalTokens;
    private Long waitingTokens;
    private Long servingTokens;
    private Long servedTokens;
    private Long activeCounters;
    private Map<String, Long> tokensByQueue;
    private Map<String, Long> averageWaitTimeByQueue;
}
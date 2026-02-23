package com.example.queue.service;

import com.example.queue.dto.CounterRequest;
import com.example.queue.dto.QueueRequest;
import com.example.queue.dto.StatsResponse;
import com.example.queue.model.Counter;
import com.example.queue.model.Queue;
import com.example.queue.model.Token;
import com.example.queue.repository.CounterRepository;
import com.example.queue.repository.QueueRepository;
import com.example.queue.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final TokenRepository tokenRepository;
    private final CounterRepository counterRepository;
    private final QueueRepository queueRepository;
    
    public StatsResponse getStats() {
        List<Token> allTokens = tokenRepository.findAll();
        List<Token> waitingTokens = tokenRepository.findByStatus(Token.TokenStatus.WAITING);
        List<Token> servingTokens = tokenRepository.findByStatus(Token.TokenStatus.SERVING);
        List<Token> servedTokens = tokenRepository.findByStatus(Token.TokenStatus.SERVED);
        
        long activeCounters = counterRepository.findByStatus(Counter.CounterStatus.BUSY).size();
        
        // Group tokens by queue
        Map<String, Long> tokensByQueue = new HashMap<>();
        Map<String, Long> averageWaitTimeByQueue = new HashMap<>();
        
        allTokens.stream()
            .filter(t -> t.getQueue() != null)
            .forEach(token -> {
                String queueName = token.getQueue().getName();
                tokensByQueue.merge(queueName, 1L, Long::sum);
            });
        
        return new StatsResponse(
            (long) allTokens.size(),
            (long) waitingTokens.size(),
            (long) servingTokens.size(),
            (long) servedTokens.size(),
            activeCounters,
            tokensByQueue,
            averageWaitTimeByQueue
        );
    }

    @Transactional
    public Queue createQueue(QueueRequest request) {
        if (queueRepository.findByName(request.getName()).isPresent()) {
            throw new RuntimeException("Queue with name '" + request.getName() + "' already exists");
        }
        Queue queue = new Queue();
        queue.setName(request.getName());
        queue.setDepartment(request.getDepartment());
        return queueRepository.save(queue);
    }

    @Transactional
    public Counter createCounter(CounterRequest request) {
        Queue queue = queueRepository.findById(request.getQueueId())
                .orElseThrow(() -> new RuntimeException("Queue not found"));
        if (counterRepository.findByCounterNumberAndQueueId(request.getCounterNumber(), request.getQueueId()).isPresent()) {
            throw new RuntimeException("Counter " + request.getCounterNumber() + " already exists for this queue");
        }
        Counter counter = new Counter();
        counter.setCounterNumber(request.getCounterNumber());
        counter.setQueue(queue);
        counter.setStatus(Counter.CounterStatus.AVAILABLE);
        return counterRepository.save(counter);
    }
}
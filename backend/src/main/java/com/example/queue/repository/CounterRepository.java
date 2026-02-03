package com.example.queue.repository;

import com.example.queue.model.Counter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CounterRepository extends JpaRepository<Counter, Long> {
    List<Counter> findByQueueId(Long queueId);
    Optional<Counter> findByCounterNumberAndQueueId(Integer counterNumber, Long queueId);
    List<Counter> findByStatus(Counter.CounterStatus status);
}
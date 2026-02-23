package com.example.queue.controller;

import com.example.queue.dto.CounterRequest;
import com.example.queue.dto.CounterResponse;
import com.example.queue.dto.QueueRequest;
import com.example.queue.dto.QueueResponse;
import com.example.queue.dto.StatsResponse;
import com.example.queue.model.Counter;
import com.example.queue.model.Queue;
import com.example.queue.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        StatsResponse stats = adminService.getStats();
        return ResponseEntity.ok(stats);
    }

    @PostMapping("/queues")
    public ResponseEntity<QueueResponse> createQueue(@Valid @RequestBody QueueRequest request) {
        Queue queue = adminService.createQueue(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new QueueResponse(queue.getId(), queue.getName(), queue.getDepartment()));
    }

    @PostMapping("/counters")
    public ResponseEntity<CounterResponse> createCounter(@Valid @RequestBody CounterRequest request) {
        Counter counter = adminService.createCounter(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CounterResponse(
                        counter.getId(),
                        counter.getCounterNumber(),
                        counter.getQueue().getId(),
                        counter.getStatus().name(),
                        counter.getCurrentTokenId(),
                        null));
    }
}

package com.example.queue.controller;

import com.example.queue.dto.CounterResponse;
import com.example.queue.model.Token;
import com.example.queue.service.CounterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/counters")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CounterController {

    private final CounterService counterService;

    @GetMapping
    public ResponseEntity<List<CounterResponse>> getAllCounters() {
        List<CounterResponse> counters = counterService.getAllCounters();
        return ResponseEntity.ok(counters);
    }

    @GetMapping("/queue/{queueId}")
    public ResponseEntity<List<CounterResponse>> getCountersByQueue(@PathVariable Long queueId) {
        List<CounterResponse> counters = counterService.getCountersByQueue(queueId);
        return ResponseEntity.ok(counters);
    }

    @PostMapping("/{counterId}/serve-next")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> serveNextToken(@PathVariable Long counterId) {
        Token token = counterService.serveNextToken(counterId);
        return ResponseEntity.ok(Map.of(
                "tokenId", token.getId(),
                "tokenNumber", token.getTokenNumber(),
                "message", "Token " + token.getTokenNumber() + " is now being served"
        ));
    }

    @PostMapping("/{counterId}/complete/{tokenId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> completeToken(
            @PathVariable Long counterId,
            @PathVariable Long tokenId) {
        counterService.completeToken(counterId, tokenId);
        return ResponseEntity.ok(Map.of("message", "Token served successfully"));
    }
}

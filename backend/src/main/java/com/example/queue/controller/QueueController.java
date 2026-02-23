package com.example.queue.controller;

import com.example.queue.dto.QueueResponse;
import com.example.queue.dto.QueueStatusResponse;
import com.example.queue.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/queues")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QueueController {

    private final QueueService queueService;

    @GetMapping
    public ResponseEntity<List<QueueResponse>> getAllQueues() {
        List<QueueResponse> queues = queueService.getAllQueues();
        return ResponseEntity.ok(queues);
    }

    @GetMapping("/status/{tokenId}")
    public ResponseEntity<QueueStatusResponse> getQueueStatus(@PathVariable Long tokenId) {
        QueueStatusResponse status = queueService.getQueueStatus(tokenId);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/{queueId}/current-serving")
    public ResponseEntity<Integer> getCurrentServingToken(@PathVariable Long queueId) {
        Integer currentToken = queueService.getCurrentServingToken(queueId);
        return ResponseEntity.ok(currentToken != null ? currentToken : 0);
    }
}

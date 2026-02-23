package com.example.queue.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class QueueRequest {
    @NotBlank(message = "Queue name is required")
    private String name;

    @NotBlank(message = "Department is required")
    private String department;
}

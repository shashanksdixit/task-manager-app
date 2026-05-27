package com.example.taskmanager.dto;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TaskDto(
        Long id,
        String title,
        String description,
        Priority priority,
        Status status,
        LocalDate dueDate,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

package com.example.taskmanager.dto;

import com.example.taskmanager.model.Priority;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(
        @NotBlank String title,
        String description,
        Priority priority,
        LocalDate dueDate
) {
}

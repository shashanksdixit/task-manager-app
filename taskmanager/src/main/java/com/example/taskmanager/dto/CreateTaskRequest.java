package com.example.taskmanager.dto;

import com.example.taskmanager.model.Priority;
import java.time.LocalDate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateTaskRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,
        @Size(max = 2000, message = "Description must not exceed 2000 characters")
        String description,
        Priority priority,
        LocalDate dueDate
) {
}

package com.example.taskmanager.dto;

import com.example.taskmanager.model.Status;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateDto(
        @NotNull Status status
) {
}

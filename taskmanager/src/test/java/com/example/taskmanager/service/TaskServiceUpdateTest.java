package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceUpdateTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void update_ShouldReturnUpdatedTaskDto_WhenTaskExists() {
        Task existingTask = new Task();
        existingTask.setId(1L);
        existingTask.setTitle("Original Title");
        existingTask.setDescription("Original Description");
        existingTask.setPriority(Priority.HIGH);
        existingTask.setStatus(Status.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(existingTask));

        Task updatedTask = new Task();
        updatedTask.setId(1L);
        updatedTask.setTitle("Updated Title");
        updatedTask.setDescription("New desc");
        updatedTask.setPriority(Priority.LOW);
        updatedTask.setStatus(Status.TODO);

        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        TaskDto updatedDto = new TaskDto(1L, "Updated Title", "New desc", Priority.LOW,
                Status.TODO, null, null, null);
        when(taskMapper.toDto(any(Task.class))).thenReturn(updatedDto);

        TaskDto result = taskService.update(1L,
                new UpdateTaskRequest("Updated Title", "New desc", Priority.LOW, null));

        assertNotNull(result);
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.update(99L,
                        new UpdateTaskRequest("Updated Title", "New desc", Priority.LOW, null)));
    }
}

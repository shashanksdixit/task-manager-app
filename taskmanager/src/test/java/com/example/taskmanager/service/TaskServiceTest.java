package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.mapper.TaskMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void create_ShouldReturnTaskDto_WhenValidRequest() {
        CreateTaskRequest request = new CreateTaskRequest("Test Task", null, Priority.HIGH, null);

        Task entity = new Task();
        entity.setTitle("Test Task");
        entity.setPriority(Priority.HIGH);

        when(taskMapper.toEntity(request)).thenReturn(entity);

        Task saved = new Task();
        saved.setTitle("Test Task");
        saved.setPriority(Priority.HIGH);
        saved.setId(1L);

        when(taskRepository.save(any(Task.class))).thenReturn(saved);

        TaskDto dto = new TaskDto(1L, "Test Task", null, Priority.HIGH, Status.TODO, null, null, null);
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        TaskDto result = taskService.create(request);

        verify(taskRepository, times(1)).save(any(Task.class));
        assertNotNull(result);
    }

    @Test
    void create_ShouldThrowException_WhenTitleIsNull() {
        CreateTaskRequest request = new CreateTaskRequest(null, null, Priority.MEDIUM, null);

        when(taskMapper.toEntity(request)).thenThrow(new IllegalArgumentException("title is null"));

        assertThrows(IllegalArgumentException.class, () -> taskService.create(request));
    }
}

package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.exception.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
    void listAll_ShouldReturnListOfTaskDtos() {
        Task task1 = new Task();
        task1.setId(1L);
        task1.setTitle("Task One");

        Task task2 = new Task();
        task2.setId(2L);
        task2.setTitle("Task Two");

        List<Task> tasks = List.of(task1, task2);
        when(taskRepository.findAllByOrderByCreatedAtDesc()).thenReturn(tasks);

        TaskDto dto = new TaskDto(1L, "Task One", "Description 1", Priority.HIGH, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        List<TaskDto> result = taskService.listAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getById_ShouldReturnTaskDto_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Existing Task");

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        TaskDto dto = new TaskDto(1L, "Existing Task", "Description", Priority.MEDIUM, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any())).thenReturn(dto);

        TaskDto result = taskService.getById(1L);

        assertNotNull(result);
        verify(taskRepository, times(1)).findById(1L);
    }

    @Test
    void getById_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.getById(99L));
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void create_ShouldThrowException_WhenTitleIsNull() {
        CreateTaskRequest request = new CreateTaskRequest(null, null, Priority.MEDIUM, null);

        when(taskMapper.toEntity(request)).thenThrow(new IllegalArgumentException("title is null"));

        assertThrows(IllegalArgumentException.class, () -> taskService.create(request));
    }
}

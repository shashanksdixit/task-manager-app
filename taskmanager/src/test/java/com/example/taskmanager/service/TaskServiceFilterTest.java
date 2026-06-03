package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceFilterTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void listAll_ShouldReturnAllTasks_WhenNoFilters() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();

        List<Task> tasks = List.of(task1, task2, task3);
        when(taskRepository.findAll(any(Specification.class))).thenReturn(tasks);

        TaskDto dto = new TaskDto(1L, "Task 1", "Description", Priority.MEDIUM, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        List<TaskDto> result = taskService.listAll(null, null, null);

        assertEquals(3, result.size());
        verify(taskRepository, times(1)).findAll(any(Specification.class));
    }

    @Test
    void listAll_ShouldReturnFilteredTasks_WhenKeywordProvided() {
        Task task = new Task();
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll(any(Specification.class))).thenReturn(tasks);

        TaskDto dto = new TaskDto(1L, "Fix login bug", "Description", Priority.MEDIUM, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        List<TaskDto> result = taskService.listAll("fix", null, null);

        assertEquals(1, result.size());
        assertEquals("Fix login bug", result.get(0).title());
    }

    @Test
    void listAll_ShouldReturnEmpty_WhenNoMatchFound() {
        when(taskRepository.findAll(any(Specification.class))).thenReturn(List.of());

        List<TaskDto> result = taskService.listAll("nonexistent", null, null);

        assertTrue(result.isEmpty());
    }

    @Test
    void listAll_ShouldPassCombinedFilters_WhenAllParamsProvided() {
        Task task = new Task();
        List<Task> tasks = List.of(task);
        when(taskRepository.findAll(any(Specification.class))).thenReturn(tasks);

        TaskDto dto = new TaskDto(1L, "Fix login bug", "Description", Priority.HIGH, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        List<TaskDto> result = taskService.listAll("fix", Status.TODO, Priority.HIGH);

        assertEquals(1, result.size());
        verify(taskRepository, times(1)).findAll(any(Specification.class));
    }
}

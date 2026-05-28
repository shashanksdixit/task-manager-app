package com.example.taskmanager.service;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskStatusUpdateDto;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceStatusTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void changeStatus_ShouldReturnUpdatedTaskDto_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);
        task.setStatus(Status.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        Task saved = new Task();
        saved.setId(1L);
        saved.setStatus(Status.IN_PROGRESS);
        when(taskRepository.save(any())).thenReturn(saved);

        TaskDto dto = new TaskDto(1L, "Test", null, Priority.MEDIUM, Status.IN_PROGRESS, null, null, null);
        when(taskMapper.toDto(any())).thenReturn(dto);

        TaskDto result = taskService.changeStatus(1L, new TaskStatusUpdateDto(Status.IN_PROGRESS));

        assertNotNull(result);
        assertEquals(Status.IN_PROGRESS, result.status());
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void changeStatus_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> taskService.changeStatus(99L, new TaskStatusUpdateDto(Status.COMPLETE)));

        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void changeStatus_ShouldUpdateOnlyStatus_NotOtherFields() {
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Original");
        task.setPriority(Priority.HIGH);
        task.setStatus(Status.TODO);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TaskDto dto = new TaskDto(1L, "Original", null, Priority.HIGH, Status.COMPLETE, null, null, null);
        when(taskMapper.toDto(any())).thenReturn(dto);

        TaskDto result = taskService.changeStatus(1L, new TaskStatusUpdateDto(Status.COMPLETE));

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository, times(1)).save(captor.capture());

        Task saved = captor.getValue();
        assertEquals("Original", saved.getTitle());
        assertEquals(Status.COMPLETE, saved.getStatus());
    }
}

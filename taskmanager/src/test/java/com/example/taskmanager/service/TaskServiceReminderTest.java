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
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceReminderTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void getUrgentTasks_ShouldReturnUrgentTasks() {
        Task task = new Task();
        List<Task> tasks = List.of(task);
        when(taskRepository.findByDueDateBetweenAndStatusNot(any(LocalDate.class), any(LocalDate.class), eq(Status.COMPLETE)))
                .thenReturn(tasks);

        TaskDto dto = new TaskDto(1L, "Pay rent", "Description", Priority.HIGH, Status.TODO,
                LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        when(taskMapper.toDto(any(Task.class))).thenReturn(dto);

        List<TaskDto> result = taskService.getUrgentTasks();

        assertEquals(1, result.size());
        assertEquals("Pay rent", result.get(0).title());
        verify(taskRepository, times(1))
                .findByDueDateBetweenAndStatusNot(any(LocalDate.class), any(LocalDate.class), eq(Status.COMPLETE));
    }

    @Test
    void getUrgentTasks_ShouldQueryTodayAndTomorrowExcludingComplete() {
        when(taskRepository.findByDueDateBetweenAndStatusNot(any(LocalDate.class), any(LocalDate.class), eq(Status.COMPLETE)))
                .thenReturn(List.of());

        taskService.getUrgentTasks();

        ArgumentCaptor<LocalDate> startCaptor = ArgumentCaptor.forClass(LocalDate.class);
        ArgumentCaptor<LocalDate> endCaptor = ArgumentCaptor.forClass(LocalDate.class);
        verify(taskRepository).findByDueDateBetweenAndStatusNot(startCaptor.capture(), endCaptor.capture(), eq(Status.COMPLETE));

        assertEquals(LocalDate.now(), startCaptor.getValue());
        assertEquals(LocalDate.now().plusDays(1), endCaptor.getValue());
    }

    @Test
    void getUrgentTasks_ShouldReturnEmpty_WhenNoMatchFound() {
        when(taskRepository.findByDueDateBetweenAndStatusNot(any(LocalDate.class), any(LocalDate.class), eq(Status.COMPLETE)))
                .thenReturn(List.of());

        List<TaskDto> result = taskService.getUrgentTasks();

        assertTrue(result.isEmpty());
    }
}

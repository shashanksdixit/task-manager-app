package com.example.taskmanager.service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.repository.TaskRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceDeleteTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;

    @Test
    void delete_ShouldCallRepositoryDelete_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        taskService.delete(1L);

        verify(taskRepository, times(1)).findById(1L);
        verify(taskRepository).deleteById(1L);
    }

    @Test
    void delete_ShouldThrowEntityNotFoundException_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> taskService.delete(99L));

        verify(taskRepository, times(1)).findById(99L);
        verify(taskRepository, never()).deleteById(any());
    }

    @Test
    void delete_ShouldLogTaskId_WhenTaskExists() {
        Task task = new Task();
        task.setId(1L);

        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        ListAppender<ILoggingEvent> appender = attachAppender();
        try {
            taskService.delete(1L);
        } finally {
            taskLogger().detachAppender(appender);
        }

        assertEquals(1, appender.list.size());
        ILoggingEvent event = appender.list.get(0);
        assertEquals(Level.INFO, event.getLevel());
        assertEquals("The task id 1 is deleted", event.getFormattedMessage());
    }

    @Test
    void delete_ShouldNotLogDeletion_WhenTaskNotFound() {
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        ListAppender<ILoggingEvent> appender = attachAppender();
        try {
            assertThrows(EntityNotFoundException.class, () -> taskService.delete(99L));
        } finally {
            taskLogger().detachAppender(appender);
        }

        assertTrue(appender.list.isEmpty());
    }

    private static Logger taskLogger() {
        return (Logger) LoggerFactory.getLogger(TaskService.class);
    }

    private static ListAppender<ILoggingEvent> attachAppender() {
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        taskLogger().addAppender(appender);
        return appender;
    }
}

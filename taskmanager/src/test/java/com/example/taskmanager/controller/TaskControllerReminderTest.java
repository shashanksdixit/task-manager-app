package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.service.TaskService;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerReminderTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void getUrgentTasks_ShouldReturn200_WithTasks() throws Exception {
        TaskDto task = new TaskDto(1L, "Pay rent", "Description", Priority.HIGH, Status.TODO,
                LocalDate.now(), null, null);

        Mockito.when(taskService.getUrgentTasks()).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Pay rent"));
    }

    @Test
    void getUrgentTasks_ShouldReturn200_WithEmptyList_WhenNone() throws Exception {
        Mockito.when(taskService.getUrgentTasks()).thenReturn(List.of());

        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}

package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void getAllTasks_ShouldReturn200_WithNoFilters() throws Exception {
        TaskDto task1 = new TaskDto(1L, "Fix bug A", "Description A", Priority.HIGH, Status.TODO, null, null, null);
        TaskDto task2 = new TaskDto(2L, "Write docs", "Description B", Priority.MEDIUM, Status.IN_PROGRESS, null, null, null);
        TaskDto task3 = new TaskDto(3L, "Review PR", "Description C", Priority.LOW, Status.DONE, null, null, null);

        Mockito.when(taskService.listAll(null, null, null)).thenReturn(List.of(task1, task2, task3));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void getAllTasks_ShouldReturn200_WithKeywordFilter() throws Exception {
        TaskDto task = new TaskDto(1L, "Fix login issue", "Description fix", Priority.MEDIUM, Status.IN_PROGRESS, null, null, null);

        Mockito.when(taskService.listAll(eq("fix"), isNull(), isNull())).thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks").param("keyword", "fix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getAllTasks_ShouldReturn200_WithStatusFilter() throws Exception {
        TaskDto task1 = new TaskDto(1L, "Fix bug A", "Description A", Priority.HIGH, Status.TODO, null, null, null);
        TaskDto task2 = new TaskDto(2L, "Fix bug B", "Description B", Priority.MEDIUM, Status.TODO, null, null, null);

        Mockito.when(taskService.listAll(isNull(), eq(Status.TODO), isNull())).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks").param("status", "TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getAllTasks_ShouldReturn200_WithCombinedFilters() throws Exception {
        TaskDto task = new TaskDto(1L, "Fix bug A", "Description A", Priority.HIGH, Status.TODO, null, null, null);

        Mockito.when(taskService.listAll(eq("fix"), eq(Status.TODO), eq(Priority.HIGH)))
                .thenReturn(List.of(task));

        mockMvc.perform(get("/api/tasks")
                        .param("keyword", "fix")
                        .param("status", "TODO")
                        .param("priority", "HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }
}

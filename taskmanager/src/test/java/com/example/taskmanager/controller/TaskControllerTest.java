package com.example.taskmanager.controller;

import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void createTask_ShouldReturn201_WhenValidRequest() throws Exception {
        TaskDto created = new TaskDto(1L, "Test Task", null, Priority.HIGH, Status.TODO, null, null, null);

        Mockito.when(taskService.create(any())).thenReturn(created);

        String requestJson = "{" +
                "\"title\":\"Test Task\"," +
                "\"priority\":\"HIGH\"," +
                "\"description\":\"Test description\"" +
                "}";

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Test Task"))
                .andExpect(jsonPath("$.status").value("TODO"));
    }

    @Test
    void createTask_ShouldReturn400_WhenTitleMissing() throws Exception {
        String requestJson = "{" +
                "\"description\":\"No title here\"" +
                "}";

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllTasks_ShouldReturn200_WithTaskList() throws Exception {
        TaskDto task1 = new TaskDto(1L, "Task One", "Description one", Priority.HIGH, Status.TODO, null, null, null);
        TaskDto task2 = new TaskDto(2L, "Task Two", "Description two", Priority.MEDIUM, Status.IN_PROGRESS, null, null, null);

        Mockito.when(taskService.listAll()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getTaskById_ShouldReturn200_WhenTaskExists() throws Exception {
        TaskDto task = new TaskDto(1L, "Test Task", "Test description", Priority.HIGH, Status.TODO, null, null, null);

        Mockito.when(taskService.getById(1L)).thenReturn(task);

        mockMvc.perform(get("/api/tasks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Task"));
    }

    @Test
    void getTaskById_ShouldReturn404_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.getById(99L)).thenThrow(new EntityNotFoundException("Task not found"));

        mockMvc.perform(get("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}

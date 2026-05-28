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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerStatusTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void changeStatus_ShouldReturn200_WhenValidRequest() throws Exception {
        TaskDto updatedDto = new TaskDto(1L, "Test Task", null, Priority.HIGH, Status.IN_PROGRESS, null, null, null);

        Mockito.when(taskService.changeStatus(eq(1L), any())).thenReturn(updatedDto);

        String requestJson = "{" +
                "\"status\":\"IN_PROGRESS\"" +
                "}";

        mockMvc.perform(patch("/api/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void changeStatus_ShouldReturn404_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.changeStatus(eq(99L), any()))
                .thenThrow(new EntityNotFoundException("Task not found"));

        String requestJson = "{" +
                "\"status\":\"COMPLETE\"" +
                "}";

        mockMvc.perform(patch("/api/tasks/99/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeStatus_ShouldReturn400_WhenStatusIsNull() throws Exception {
        String requestJson = "{" +
                "\"status\":null" +
                "}";

        mockMvc.perform(patch("/api/tasks/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}

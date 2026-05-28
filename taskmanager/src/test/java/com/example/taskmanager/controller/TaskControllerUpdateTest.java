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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerUpdateTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TaskService taskService;

    @Test
    void updateTask_ShouldReturn200_WhenValidRequest() throws Exception {
        TaskDto updatedDto = new TaskDto(1L, "Updated Task", null, Priority.LOW, Status.TODO, null, null, null);

        Mockito.when(taskService.update(eq(1L), any())).thenReturn(updatedDto);

        String requestJson = "{"
                + "\"title\":\"Updated Task\"," 
                + "\"priority\":\"LOW\"," 
                + "\"description\":\"Updated desc\""
                + "}";

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Task"));
    }

    @Test
    void updateTask_ShouldReturn404_WhenTaskNotFound() throws Exception {
        Mockito.when(taskService.update(eq(99L), any()))
                .thenThrow(new EntityNotFoundException("Task not found"));

        String requestJson = "{"
                + "\"title\":\"Updated Task\"," 
                + "\"priority\":\"LOW\"," 
                + "\"description\":\"Updated desc\""
                + "}";

        mockMvc.perform(put("/api/tasks/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateTask_ShouldReturn400_WhenTitleBlank() throws Exception {
        String requestJson = "{"
                + "\"title\":\"\"," 
                + "\"description\":\"No title\""
                + "}";

        mockMvc.perform(put("/api/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}

package com.example.taskmanager.controller;

import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
public class TaskControllerDeleteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Test
    void deleteTask_ShouldReturn204_WhenTaskExists() throws Exception {
        Mockito.doNothing().when(taskService).delete(1L);

        mockMvc.perform(delete("/api/tasks/1"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
    }

    @Test
    void deleteTask_ShouldReturn404_WhenTaskNotFound() throws Exception {
        Mockito.doThrow(new EntityNotFoundException("Task not found with id: 99"))
                .when(taskService).delete(99L);

        mockMvc.perform(delete("/api/tasks/99"))
                .andExpect(status().isNotFound());
    }
}

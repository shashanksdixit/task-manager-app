package com.example.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TaskIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTask_ShouldPersistAndReturnTask() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Integration Test Task");
        request.put("description", "Integration test description");
        request.put("priority", "HIGH");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.status").value("TODO"))
                .andExpect(jsonPath("$.title").value("Integration Test Task"));
    }

    @Test
    void createTask_ShouldReturn400_WhenTitleBlank() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "");
        request.put("description", "Blank title validation test");
        request.put("priority", "HIGH");

        String requestJson = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isBadRequest());
    }
}

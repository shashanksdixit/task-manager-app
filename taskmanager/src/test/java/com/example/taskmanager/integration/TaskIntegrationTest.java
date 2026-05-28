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

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @Test
    void getAllTasks_ShouldReturnSeededTasks() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(3))));
    }

    @Test
    void getTaskById_ShouldReturnCorrectTask() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Integration Created Task");
        request.put("description", "Created for GET by id test");
        request.put("priority", "MEDIUM");

        String requestJson = objectMapper.writeValueAsString(request);

        String responseJson = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> createdTask = objectMapper.readValue(responseJson, Map.class);
        Number createdIdNumber = (Number) createdTask.get("id");
        Integer createdId = createdIdNumber.intValue();

        mockMvc.perform(get("/api/tasks/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Created Task"));
    }

    @Test
    void updateTask_ShouldPersistChanges() throws Exception {
        Map<String, Object> createRequest = new HashMap<>();
        createRequest.put("title", "Integration Update Task");
        createRequest.put("description", "Created for update test");
        createRequest.put("priority", "LOW");

        String createJson = objectMapper.writeValueAsString(createRequest);

        String createResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> createdTask = objectMapper.readValue(createResponse, Map.class);
        Number taskIdNumber = (Number) createdTask.get("id");
        Integer taskId = taskIdNumber.intValue();

        Map<String, Object> updateRequest = new HashMap<>();
        updateRequest.put("title", "Updated Integration Task");
        updateRequest.put("description", "Created for update test");
        updateRequest.put("priority", "LOW");

        String updateJson = objectMapper.writeValueAsString(updateRequest);

        mockMvc.perform(put("/api/tasks/{id}", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Integration Task"));
    }

    @Test
    void getTaskById_ShouldReturn404_ForNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound());
    }
}

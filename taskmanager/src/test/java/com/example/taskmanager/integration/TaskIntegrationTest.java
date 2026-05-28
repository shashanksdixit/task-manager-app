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
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
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
    void changeStatus_ShouldUpdateStatusAndReturn200() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Status Test Task");
        request.put("description", "Created for status update test");
        request.put("priority", "HIGH");

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
        Number taskIdNumber = (Number) createdTask.get("id");
        Integer taskId = taskIdNumber.intValue();

        Map<String, Object> statusRequest = new HashMap<>();
        statusRequest.put("status", "IN_PROGRESS");
        String statusJson = objectMapper.writeValueAsString(statusRequest);

        mockMvc.perform(patch("/api/tasks/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }

    @Test
    void changeStatus_ShouldReturn404_ForNonExistentTask() throws Exception {
        Map<String, Object> statusRequest = new HashMap<>();
        statusRequest.put("status", "COMPLETE");
        String statusJson = objectMapper.writeValueAsString(statusRequest);

        mockMvc.perform(patch("/api/tasks/99999/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void changeStatus_ShouldNotAffectOtherFields() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Field Test");
        request.put("description", "Created for field preservation test");
        request.put("priority", "HIGH");

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
        Number taskIdNumber = (Number) createdTask.get("id");
        Integer taskId = taskIdNumber.intValue();

        Map<String, Object> statusRequest = new HashMap<>();
        statusRequest.put("status", "COMPLETE");
        String statusJson = objectMapper.writeValueAsString(statusRequest);

        mockMvc.perform(patch("/api/tasks/{id}/status", taskId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(statusJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETE"));

        mockMvc.perform(get("/api/tasks/{id}", taskId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Field Test"))
                .andExpect(jsonPath("$.priority").value("HIGH"))
                .andExpect(jsonPath("$.status").value("COMPLETE"));
    }

    @Test
    void deleteTask_ShouldReturn204_AndRemoveTask() throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", "Task To Delete");
        request.put("description", "Task created for delete test");
        request.put("priority", "MEDIUM");

        String requestJson = objectMapper.writeValueAsString(request);

        String responseJson = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> createdTask = objectMapper.readValue(responseJson, Map.class);
        Number createdIdNumber = (Number) createdTask.get("id");
        Integer createdId = createdIdNumber.intValue();

        mockMvc.perform(delete("/api/tasks/{id}", createdId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", createdId))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ShouldReturn404_ForNonExistentTask() throws Exception {
        mockMvc.perform(delete("/api/tasks/99999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteTask_ShouldNotAffectOtherTasks() throws Exception {
        String initialResponse = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> initialTasks = objectMapper.readValue(initialResponse, List.class);
        int initialCount = initialTasks.size();

        Map<String, Object> request = new HashMap<>();
        request.put("title", "Task Delete Safety Test");
        request.put("description", "Created to verify delete does not affect other tasks");
        request.put("priority", "LOW");

        String requestJson = objectMapper.writeValueAsString(request);

        String createdResponse = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> createdTask = objectMapper.readValue(createdResponse, Map.class);
        Number createdIdNumber = (Number) createdTask.get("id");
        Integer createdId = createdIdNumber.intValue();

        mockMvc.perform(delete("/api/tasks/{id}", createdId))
                .andExpect(status().isNoContent());

        String afterResponse = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<?> afterTasks = objectMapper.readValue(afterResponse, List.class);
        assertEquals(initialCount, afterTasks.size());
    }

    @Test
    void getTaskById_ShouldReturn404_ForNonExistentTask() throws Exception {
        mockMvc.perform(get("/api/tasks/99999"))
                .andExpect(status().isNotFound());
    }
}

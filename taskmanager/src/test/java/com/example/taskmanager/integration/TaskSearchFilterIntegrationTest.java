package com.example.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TaskSearchFilterIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws Exception {
        // Create task 1: "Fix login bug", priority=HIGH
        Map<String, Object> task1 = new HashMap<>();
        task1.put("title", "Fix login bug");
        task1.put("priority", "HIGH");
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task1)))
                .andExpect(status().isCreated());

        // Create task 2: "Buy groceries", priority=LOW
        Map<String, Object> task2 = new HashMap<>();
        task2.put("title", "Buy groceries");
        task2.put("priority", "LOW");
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task2)))
                .andExpect(status().isCreated());

        // Create task 3: "Fix formatting issue", priority=MEDIUM
        Map<String, Object> task3 = new HashMap<>();
        task3.put("title", "Fix formatting issue");
        task3.put("priority", "MEDIUM");
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task3)))
                .andExpect(status().isCreated());

        // Create task 4: "Team standup", priority=HIGH
        Map<String, Object> task4 = new HashMap<>();
        task4.put("title", "Team standup");
        task4.put("priority", "HIGH");
        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(task4)))
                .andExpect(status().isCreated());
    }

    @Test
    void search_ShouldReturnMatchingTasks_WhenKeywordProvided() throws Exception {
        mockMvc.perform(get("/api/tasks?keyword=fix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void filter_ShouldReturnMatchingTasks_WhenStatusProvided() throws Exception {
        mockMvc.perform(get("/api/tasks?status=TODO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))));
    }

    @Test
    void filter_ShouldReturnMatchingTasks_WhenPriorityProvided() throws Exception {
        mockMvc.perform(get("/api/tasks?priority=HIGH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))));
    }

    @Test
    void search_ShouldReturnEmpty_WhenNoMatch() throws Exception {
        mockMvc.perform(get("/api/tasks?keyword=zzznomatch"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void filter_ShouldReturnAll_WhenNoFiltersProvided() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(4))));
    }
}

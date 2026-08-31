package com.example.taskmanager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
public class TaskReminderIntegrationTest {

    private static final String PREFIX = "REMINDER_TEST_";

    private static final String URGENT_TODAY = PREFIX + "due_today";
    private static final String URGENT_TOMORROW = PREFIX + "due_tomorrow";
    private static final String COMPLETE_TODAY = PREFIX + "complete_today";
    private static final String FAR_FUTURE = PREFIX + "far_future";
    private static final String NO_DUE_DATE = PREFIX + "no_due_date";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static String format(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    @BeforeEach
    void setUp() throws Exception {
        createTask(URGENT_TODAY, LocalDate.now());
        createTask(URGENT_TOMORROW, LocalDate.now().plusDays(1));
        createTask(COMPLETE_TODAY, LocalDate.now()).andReturn();
        createTask(FAR_FUTURE, LocalDate.now().plusDays(3));
        createTask(NO_DUE_DATE, null);
    }

    private ResultActions createTask(String title, LocalDate dueDate) throws Exception {
        Map<String, Object> request = new HashMap<>();
        request.put("title", title);
        request.put("priority", "HIGH");
        if (dueDate != null) {
            request.put("dueDate", format(dueDate));
        }
        return mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void urgent_ShouldIncludeTasksDueTodayOrTomorrow() throws Exception {
        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='" + URGENT_TODAY + "')]").exists())
                .andExpect(jsonPath("$[?(@.title=='" + URGENT_TOMORROW + "')]").exists());
    }

    @Test
    void urgent_ShouldExcludeTasksDueAfter24Hours() throws Exception {
        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='" + FAR_FUTURE + "')]").isEmpty());
    }

    @Test
    void urgent_ShouldExcludeTasksWithNoDueDate() throws Exception {
        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='" + NO_DUE_DATE + "')]").isEmpty());
    }

    @Test
    void urgent_ShouldExcludeCompletedTasks() throws Exception {
        // Mark the task due today as COMPLETE, then assert it is excluded from urgent.
        String allTasks = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object>[] tasks = objectMapper.readValue(allTasks, Map[].class);
        for (Map<String, Object> task : tasks) {
            if (COMPLETE_TODAY.equals(task.get("title"))) {
                int id = ((Number) task.get("id")).intValue();
                Map<String, Object> statusBody = new HashMap<>();
                statusBody.put("status", "COMPLETE");
                mockMvc.perform(patch("/api/tasks/" + id + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusBody)))
                        .andExpect(status().isOk());
                break;
            }
        }

        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='" + COMPLETE_TODAY + "')]").isEmpty());
    }

    @Test
    void urgent_ShouldReturnOnlyExpectedTitles_AmongPrefixedTasks() throws Exception {
        // Among REMINDER_TEST_ tasks, due today/tomorrow (regardless of TODO/IN_PROGRESS) are urgent;
        // far-future, no-due-date are excluded. Completed exclusion is covered in its own test.
        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.title=='" + URGENT_TODAY + "')]").exists())
                .andExpect(jsonPath("$[?(@.title=='" + URGENT_TOMORROW + "')]").exists())
                .andExpect(jsonPath("$[?(@.title=='" + FAR_FUTURE + "')]").isEmpty())
                .andExpect(jsonPath("$[?(@.title=='" + NO_DUE_DATE + "')]").isEmpty());
    }

    @Test
    void urgent_ShouldReturnEmpty_WhenAllTasksAreCompleteOrOutOfWindow() throws Exception {
        // Delete all existing tasks (including DataLoader seeds), then verify urgency is empty.
        String allTasks = mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        Map<String, Object>[] tasks = objectMapper.readValue(allTasks, Map[].class);
        for (Map<String, Object> task : tasks) {
            int id = ((Number) task.get("id")).intValue();
            mockMvc.perform(MockMvcRequestBuilders.delete("/api/tasks/" + id))
                    .andExpect(status().isNoContent());
        }

        mockMvc.perform(get("/api/tasks/urgent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }
}

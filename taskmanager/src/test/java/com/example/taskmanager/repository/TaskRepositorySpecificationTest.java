package com.example.taskmanager.repository;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TaskRepositorySpecificationTest {

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();

        Task task1 = Task.builder()
                .title("Fix login bug")
                .description("urgent fix")
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .build();

        Task task2 = Task.builder()
                .title("Buy groceries")
                .description("weekly shopping")
                .priority(Priority.LOW)
                .status(Status.COMPLETE)
                .build();

        Task task3 = Task.builder()
                .title("Prepare report")
                .description("fix formatting")
                .priority(Priority.MEDIUM)
                .status(Status.IN_PROGRESS)
                .build();

        Task task4 = Task.builder()
                .title("Team meeting")
                .description("quarterly sync")
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .build();

        taskRepository.saveAll(List.of(task1, task2, task3, task4));
    }

    @Test
    void findAll_ShouldReturnAllTasks_WhenSpecificationIsNull() {
        List<Task> result = taskRepository.findAll(Specification.where(null));

        assertEquals(4, result.size());
    }

    @Test
    void findAll_ShouldFilterByKeyword_WhenKeywordMatches() {
        Specification<Task> spec = TaskSpecification.withKeyword("fix");

        List<Task> result = taskRepository.findAll(spec);

        assertEquals(3, result.size());
    }

    @Test
    void findAll_ShouldFilterByStatus_WhenStatusMatches() {
        Specification<Task> spec = TaskSpecification.withStatus(Status.TODO);

        List<Task> result = taskRepository.findAll(spec);

        assertEquals(2, result.size());
    }

    @Test
    void findAll_ShouldFilterByPriority_WhenPriorityMatches() {
        Specification<Task> spec = TaskSpecification.withPriority(Priority.HIGH);

        List<Task> result = taskRepository.findAll(spec);

        assertEquals(2, result.size());
    }

    @Test
    void findAll_ShouldCombineFilters_WhenMultipleSpecificationsProvided() {
        Specification<Task> spec = TaskSpecification.withKeyword("fix")
                .and(TaskSpecification.withStatus(Status.TODO))
                .and(TaskSpecification.withPriority(Priority.HIGH));

        List<Task> result = taskRepository.findAll(spec);

        assertEquals(1, result.size());
    }
}

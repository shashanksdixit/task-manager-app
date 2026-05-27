package com.example.taskmanager.bootstrap;

import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements ApplicationRunner {

    private final TaskRepository taskRepository;

    public DataLoader(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        LocalDate today = LocalDate.now();

        Task buyGroceries = Task.builder()
                .title("Buy groceries")
                .priority(Priority.LOW)
                .status(Status.TODO)
                .dueDate(today.plusDays(1))
                .build();

        Task prepareReport = Task.builder()
                .title("Prepare sprint report")
                .priority(Priority.HIGH)
                .status(Status.IN_PROGRESS)
                .dueDate(today)
                .build();

        Task fixLoginBug = Task.builder()
                .title("Fix login bug")
                .priority(Priority.HIGH)
                .status(Status.TODO)
                .dueDate(today.minusDays(1))
                .build();

        taskRepository.saveAll(List.of(buyGroceries, prepareReport, fixLoginBug));
    }
}

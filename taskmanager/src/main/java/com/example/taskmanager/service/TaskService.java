package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskStatusUpdateDto;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.repository.TaskRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDto> listAll() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TaskDto getById(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TaskDto create(CreateTaskRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TaskDto update(Long id, UpdateTaskRequest request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public TaskDto changeStatus(Long id, TaskStatusUpdateDto request) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public void delete(Long id) {
        throw new UnsupportedOperationException("Not implemented");
    }

    private Task findTaskOrThrow(Long id) {
        throw new EntityNotFoundException("Task not found with id " + id);
    }
}

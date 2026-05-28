package com.example.taskmanager.service;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.TaskStatusUpdateDto;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.exception.EntityNotFoundException;
import com.example.taskmanager.mapper.TaskMapper;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.Priority;
import com.example.taskmanager.model.Status;
import com.example.taskmanager.repository.TaskRepository;
import java.util.List;
import java.util.Objects;
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
        return taskRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(taskMapper::toDto)
                .toList();
    }

    public TaskDto getById(Long id) {
        Task task = findTaskOrThrow(id);
        return taskMapper.toDto(task);
    }

    @Transactional
    public TaskDto create(CreateTaskRequest request) {
        Task entity = taskMapper.toEntity(request);

        if (request.priority() == null) {
            entity.setPriority(Priority.MEDIUM);
        }

        if (entity.getStatus() == null) {
            entity.setStatus(Status.TODO);
        }

        Task saved = taskRepository.save(entity);
        return taskMapper.toDto(saved);
    }

    @Transactional
    public TaskDto update(Long id, UpdateTaskRequest request) {
        Task task = findTaskOrThrow(id);
        taskMapper.updateEntity(request, task);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    public TaskDto changeStatus(Long id, TaskStatusUpdateDto request) {
        Task task = findTaskOrThrow(id);
        task.setStatus(request.status());
        Task savedTask = taskRepository.save(task);
        return taskMapper.toDto(savedTask);
    }

    @Transactional
    public void delete(Long id) {
        Task task = findTaskOrThrow(id);
        taskRepository.delete(task);
    }

    private Task findTaskOrThrow(Long id) {
        return taskRepository.findById(Objects.requireNonNull(id))
                .orElseThrow(() -> new EntityNotFoundException("Task not found with id: " + id));
    }
}

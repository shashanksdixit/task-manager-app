package com.example.taskmanager.mapper;

import com.example.taskmanager.dto.CreateTaskRequest;
import com.example.taskmanager.dto.TaskDto;
import com.example.taskmanager.dto.UpdateTaskRequest;
import com.example.taskmanager.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TaskMapper {

    TaskMapper INSTANCE = Mappers.getMapper(TaskMapper.class);

    TaskDto toDto(Task task);

    Task toEntity(CreateTaskRequest request);

    void updateEntity(UpdateTaskRequest request, @MappingTarget Task task);
}

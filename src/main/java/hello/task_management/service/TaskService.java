package hello.task_management.service;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.DeleteTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(CreateTaskDto createTaskDto);

    TaskResponseDto findTaskById(long id);

    List<TaskResponseDto> findAllTasks();

    TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto);

    void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto);
}

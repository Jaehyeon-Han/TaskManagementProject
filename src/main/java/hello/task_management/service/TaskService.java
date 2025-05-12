package hello.task_management.service;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.DeleteTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface TaskService {
    TaskResponseDto createTask(CreateTaskDto createTaskDto);

    TaskResponseDto findTaskById(long id);

    List<TaskResponseDto> findAllTasks(String author, LocalDate lastModifiedDate);

    TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto);

    void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto);
}

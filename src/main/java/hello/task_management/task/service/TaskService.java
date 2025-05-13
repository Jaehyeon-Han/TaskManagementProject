package hello.task_management.task.service;

import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.DeleteTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import hello.task_management.task.dto.response.PagedTaskResponse;
import hello.task_management.task.dto.response.TaskResponseDto;

import java.time.LocalDate;

public interface TaskService {
    TaskResponseDto createTask(CreateTaskDto createTaskDto);

    TaskResponseDto findTaskById(long id);

    PagedTaskResponse findAllTasks(String author, LocalDate lastModifiedDate, int page, int size);

    TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto);

    void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto);
}

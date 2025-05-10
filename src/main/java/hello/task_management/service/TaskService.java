package hello.task_management.service;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;

import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(CreateTaskDto createTaskDto);

    TaskResponseDto findTaskById(long id);

    public List<TaskResponseDto> findAllTasks();
}

package hello.task_management.repository;

import hello.task_management.dto.TaskDto;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    long createTask(TaskDto taskDto);

    Optional<TaskDto> findTaskById(long createdTaskId);

    List<TaskDto> findAllTasks();

    int updateTask(TaskDto taskDto);

    void deleteTaskById(long taskId);
}

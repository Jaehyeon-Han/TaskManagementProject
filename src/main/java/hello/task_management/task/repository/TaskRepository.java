package hello.task_management.task.repository;

import hello.task_management.task.dto.TaskDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    long createTask(TaskDto taskDto);

    Optional<TaskDto> findTaskById(long createdTaskId);

    List<TaskDto> findAllTasks(String authorName, LocalDate lastModifiedDate);

    int updateTask(TaskDto taskDto);

    void deleteTaskById(long taskId);
}

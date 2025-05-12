package hello.task_management.repository;

import hello.task_management.dto.TaskDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TaskRepository {
    long createTask(TaskDto taskDto);

    Optional<TaskDto> findTaskById(long createdTaskId);

    List<TaskDto> findAllTasks(String authorName, LocalDate lastModifiedDate);

    int updateTask(TaskDto taskDto);
}

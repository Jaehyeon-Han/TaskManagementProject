package hello.task_management.service;

import hello.task_management.dto.TaskDto;
import hello.task_management.dto.TaskDtoMapper;
import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.DeleteTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.dto.response.TaskResponseDtoMapper;
import hello.task_management.exception.TaskNotFoundException;
import hello.task_management.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static hello.task_management.service.validation.PasswordMatcher.*;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(CreateTaskDto createTaskDto) {
        TaskDto newTaskDto = TaskDtoMapper.fromCreateTaskDto(createTaskDto);
        long createdTaskId = taskRepository.createTask(newTaskDto);

        TaskDto createdTask = findByIdOrThrowTaskNotFound(createdTaskId);
        return mapTaskDtoToTaskResponseDto(createdTask);
    }

    @Override
    public TaskResponseDto findTaskById(long taskId) {
        TaskDto foundTask = findByIdOrThrowTaskNotFound(taskId);
        return mapTaskDtoToTaskResponseDto(foundTask);
    }

    @Override
    public List<TaskResponseDto> findAllTasks(String author, LocalDate lastModifiedDate) {
        List<TaskDto> allTasks = taskRepository.findAllTasks(author, lastModifiedDate);
        return allTasks.stream().map(TaskResponseDtoMapper::fromTaskDto).toList();
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto) {
        TaskDto taskDto = findByIdOrThrowTaskNotFound(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(updateTaskDto.getPassword(), taskDto.getPassword());

        String modifiedTask = updateTaskDto.getTask();
        if(modifiedTask != null) {
            taskDto.setTask(modifiedTask);
        }

        String modifiedAuthor = updateTaskDto.getAuthor();
        if(modifiedAuthor != null) {
            taskDto.setAuthor(modifiedAuthor);
        }

        taskRepository.updateTask(taskDto);

        TaskDto updatedTaskDto = findByIdOrThrowTaskNotFound(taskId);
        return mapTaskDtoToTaskResponseDto(updatedTaskDto);
    }

    @Override
    public void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto) {
        TaskDto foundTaskDto = findByIdOrThrowTaskNotFound(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(deleteTaskDto.getPassword(), foundTaskDto.getPassword());

        taskRepository.deleteTaskById(taskId);
    }

    private TaskDto findByIdOrThrowTaskNotFound(long taskId) {
        return taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task for id " + taskId + " does not exist"));
    }

    private TaskResponseDto mapTaskDtoToTaskResponseDto(TaskDto taskDto) {
        return TaskResponseDtoMapper.fromTaskDto(taskDto);
    }
}

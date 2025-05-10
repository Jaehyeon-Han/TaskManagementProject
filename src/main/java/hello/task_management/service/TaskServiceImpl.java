package hello.task_management.service;

import hello.task_management.dto.TaskDto;
import hello.task_management.dto.TaskDtoMapper;
import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.dto.response.TaskResponseDtoMapper;
import hello.task_management.exception.PasswordMismatchException;
import hello.task_management.exception.TaskNotFoundException;
import hello.task_management.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(CreateTaskDto createTaskDto) {
        TaskDto newTaskDto = TaskDtoMapper.fromCreateTaskDto(createTaskDto);
        long createdTaskId = taskRepository.createTask(newTaskDto);

        TaskDto createdTask = findByIdOrThrow(createdTaskId);
        return mapTaskDtoToTaskResponseDto(createdTask);
    }

    public TaskResponseDto findTaskById(long taskId) {
        TaskDto foundTask = findByIdOrThrow(taskId);
        return mapTaskDtoToTaskResponseDto(foundTask);
    }

    public List<TaskResponseDto> findAllTasks() {
        List<TaskDto> allTasks = taskRepository.findAllTasks();
        return allTasks.stream().map(TaskResponseDtoMapper::fromTaskDto).toList();
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto) {
        TaskDto taskDto = findByIdOrThrow(taskId);

        if(!updateTaskDto.getPassword().equals(taskDto.getPassword())) {
            throw new PasswordMismatchException("password does not match");
        }

        String modifiedTask = updateTaskDto.getTask();
        if(modifiedTask != null) {
            taskDto.setTask(modifiedTask);
        }

        String modifiedAuthor = updateTaskDto.getAuthor();
        if(modifiedAuthor != null) {
            taskDto.setAuthor(modifiedAuthor);
        }

        taskRepository.updateTask(taskDto);

        TaskDto updatedTaskDto = findByIdOrThrow(taskId);
        return mapTaskDtoToTaskResponseDto(updatedTaskDto);
    }

    private TaskDto findByIdOrThrow(long taskId) {
        return taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task for id " + taskId + " does not exist"));
    }

    private TaskResponseDto mapTaskDtoToTaskResponseDto(TaskDto taskDto) {
        return TaskResponseDtoMapper.fromTaskDto(taskDto);
    }
}

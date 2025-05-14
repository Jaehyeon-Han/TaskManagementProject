package hello.task_management.task.service;

import hello.task_management.global.error.exception.TaskNotFoundException;
import hello.task_management.task.dto.TaskDto;
import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.DeleteTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import hello.task_management.task.dto.response.PagedTaskResponse;
import hello.task_management.task.dto.response.TaskResponseDto;
import hello.task_management.task.repository.TaskRepository;
import hello.task_management.user.service.UserAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static hello.task_management.global.validation.PasswordMatcher.checkPasswordMatchOrThrowPasswordMismatch;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements hello.task_management.task.service.TaskService {
    private final TaskRepository taskRepository;
    private final UserAuthenticationService userAuthenticationService;

    @Override
    public PagedTaskResponse findAllTasks(String author, LocalDate lastModifiedDate, int page, int size) {
        int offset = page * size;

        List<TaskDto> allTasks = taskRepository.findAllTasks(author, lastModifiedDate, size, offset);
        List<TaskResponseDto> pagedTasks = allTasks.stream().map(TaskResponseDto::fromTaskDto).toList();

        long totalElements = taskRepository.countTasks();

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PagedTaskResponse(pagedTasks, page, size, totalElements, totalPages);
    }

    @Override
    public TaskResponseDto createTask(CreateTaskDto createTaskDto) {
        long userId = createTaskDto.getAuthorId();
        String userPassword = createTaskDto.getAuthorPassword();

        userAuthenticationService.authenticateUserOrThrowUserExceptions(userId, userPassword);

        TaskDto newTaskDto = TaskDto.fromCreateTaskDto(createTaskDto);
        long createdTaskId = taskRepository.createTask(newTaskDto);

        TaskDto createdTask = findTaskByIdOrThrowTaskNotFoundException(createdTaskId);
        return mapTaskDtoToTaskResponseDto(createdTask);
    }

    @Override
    public TaskResponseDto findTaskById(long taskId) {
        TaskDto foundTask = findTaskByIdOrThrowTaskNotFoundException(taskId);
        return mapTaskDtoToTaskResponseDto(foundTask);
    }

    @Override
    @Transactional
    public TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto) {
        TaskDto taskDto = findTaskByIdOrThrowTaskNotFoundException(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(updateTaskDto.getTaskPassword(), taskDto.getPassword());

        String modifiedTask = updateTaskDto.getTask();
        if (modifiedTask != null) {
            taskDto.setTask(modifiedTask);
        }

        taskRepository.updateTask(taskDto.getTask(), taskDto.getId());

        TaskDto updatedTaskDto = findTaskByIdOrThrowTaskNotFoundException(taskId);
        return mapTaskDtoToTaskResponseDto(updatedTaskDto);
    }

    @Override
    public void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto) {
        TaskDto foundTaskDto = findTaskByIdOrThrowTaskNotFoundException(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(deleteTaskDto.getTaskPassword(), foundTaskDto.getPassword());

        taskRepository.deleteTaskById(taskId);
    }

    private TaskDto findTaskByIdOrThrowTaskNotFoundException(long taskId) {
        return taskRepository.findTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException("Task for id " + taskId + " does not exist"));
    }

    private TaskResponseDto mapTaskDtoToTaskResponseDto(TaskDto taskDto) {
        return TaskResponseDto.fromTaskDto(taskDto);
    }

}

package hello.task_management.task.service;

import hello.task_management.global.error.exception.UserNotFoundException;
import hello.task_management.global.error.exception.UserPasswordMismatchException;
import hello.task_management.task.dto.TaskDto;
import hello.task_management.task.dto.TaskDtoMapper;
import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.DeleteTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import hello.task_management.task.dto.response.PagedTaskResponse;
import hello.task_management.task.dto.response.TaskResponseDto;
import hello.task_management.task.dto.response.TaskResponseDtoMapper;
import hello.task_management.global.error.exception.TaskNotFoundException;
import hello.task_management.task.repository.TaskRepository;
import hello.task_management.user.dto.UserDto;
import hello.task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static hello.task_management.global.validation.PasswordMatcher.checkPasswordMatchOrThrowPasswordMismatch;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements hello.task_management.task.service.TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public PagedTaskResponse findAllTasks(String author, LocalDate lastModifiedDate, int page, int size) {
        int offset = page * size;

        List<TaskDto> allTasks = taskRepository.findAllTasks(author, lastModifiedDate, size, offset);
        List<TaskResponseDto> pagedTasks = allTasks.stream().map(TaskResponseDtoMapper::fromTaskDto).toList();

        long totalElements = taskRepository.countTasks();

        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PagedTaskResponse(pagedTasks, page, size, totalElements, totalPages);
    }

    @Override
    public TaskResponseDto createTask(CreateTaskDto createTaskDto) {
        authenticateAuthorOrThrowUserPasswordMismatchException(createTaskDto.getAuthorId(), createTaskDto.getAuthorPassword());

        TaskDto newTaskDto = TaskDtoMapper.fromCreateTaskDto(createTaskDto);
        long createdTaskId = taskRepository.createTask(newTaskDto);

        TaskDto createdTask = findByIdOrThrowTaskNotFound(createdTaskId);
        return mapTaskDtoToTaskResponseDto(createdTask);
    }

    private void authenticateAuthorOrThrowUserPasswordMismatchException(Long authorId, String authorPassword) {
        Optional<UserDto> optionalFoundUser = userRepository.findUserById(authorId);

        if(optionalFoundUser.isEmpty()) {
            throw new UserNotFoundException("User for id : " + authorId + "does not exist");
        }

        if(!authorPassword.equals(optionalFoundUser.get().getPassword())) {
            throw new UserPasswordMismatchException("user password does not match");
        }
    }

    @Override
    public TaskResponseDto findTaskById(long taskId) {
        TaskDto foundTask = findByIdOrThrowTaskNotFound(taskId);
        return mapTaskDtoToTaskResponseDto(foundTask);
    }


    @Override
    @Transactional
    public TaskResponseDto updateTaskById(long taskId, UpdateTaskDto updateTaskDto) {
        TaskDto taskDto = findByIdOrThrowTaskNotFound(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(updateTaskDto.getTaskPassword(), taskDto.getPassword());

        String modifiedTask = updateTaskDto.getTask();
        if(modifiedTask != null) {
            taskDto.setTask(modifiedTask);
        }

        taskRepository.updateTask(taskDto);

        TaskDto updatedTaskDto = findByIdOrThrowTaskNotFound(taskId);
        return mapTaskDtoToTaskResponseDto(updatedTaskDto);
    }

    @Override
    public void deleteTaskById(long taskId, DeleteTaskDto deleteTaskDto) {
        TaskDto foundTaskDto = findByIdOrThrowTaskNotFound(taskId);

        checkPasswordMatchOrThrowPasswordMismatch(deleteTaskDto.getTaskPassword(), foundTaskDto.getPassword());

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

package hello.task_management.service;

import hello.task_management.dto.TaskDto;
import hello.task_management.dto.TaskDtoMapper;
import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.dto.response.TaskResponseDtoMapper;
import hello.task_management.exception.TaskNotFoundException;
import hello.task_management.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(CreateTaskDto createTaskDto) {
        TaskDto newTaskDto = TaskDtoMapper.fromCreateTaskDto(createTaskDto);

        long createdTaskId = taskRepository.createTask(newTaskDto);
        Optional<TaskDto> optionalFoundTaskDto = taskRepository.findTaskById(createdTaskId);

        if(optionalFoundTaskDto.isEmpty()) {
            throw new TaskNotFoundException("Task for id " + createdTaskId + " does not exist");
        }

        return TaskResponseDtoMapper.fromTaskDto(optionalFoundTaskDto.get());
    }

    public TaskResponseDto findTaskById(long id) {
        Optional<TaskDto> optionalFoundTaskDto = taskRepository.findTaskById(id);

        if(optionalFoundTaskDto.isEmpty()) {
            throw new TaskNotFoundException("Task for id " + id + " does not exist");
        }

        return TaskResponseDtoMapper.fromTaskDto(optionalFoundTaskDto.get());
    }

    public List<TaskResponseDto> findAllTasks() {
        List<TaskDto> allTasks = taskRepository.findAllTasks();
        return allTasks.stream().map(TaskResponseDtoMapper::fromTaskDto).toList();
    }
}

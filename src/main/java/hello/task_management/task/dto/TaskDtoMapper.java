package hello.task_management.task.dto;

import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;

public class TaskDtoMapper {
    public static TaskDto fromCreateTaskDto(CreateTaskDto createTaskDto) {
        TaskDto taskDto = new TaskDto();

        String task = createTaskDto.getTask();
        String author = createTaskDto.getAuthor();
        String password = createTaskDto.getPassword();

        taskDto.setTask(task);
        taskDto.setAuthor(author);
        taskDto.setPassword(password);

        return taskDto;
    }

    public static TaskDto fromUpdateTaskDto(UpdateTaskDto updateTaskDto) {
        TaskDto taskDto = new TaskDto();

        String task = updateTaskDto.getTask();
        String author = updateTaskDto.getAuthor();
        String password = updateTaskDto.getPassword();

        taskDto.setTask(task);
        taskDto.setAuthor(author);
        taskDto.setPassword(password);

        return taskDto;
    }
}

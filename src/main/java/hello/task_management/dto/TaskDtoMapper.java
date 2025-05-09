package hello.task_management.dto;

import hello.task_management.dto.request.CreateTaskDto;

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
}

package hello.task_management.task.dto;

import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;

    private String task;
    private long authorId;
    private String authorName;

    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static TaskDto fromCreateTaskDto(CreateTaskDto createTaskDto) {
        TaskDto taskDto = new TaskDto();

        String task = createTaskDto.getTask();
        long authorId = createTaskDto.getAuthorId();
        String password = createTaskDto.getTaskPassword();

        taskDto.setTask(task);
        taskDto.setAuthorId(authorId);
        taskDto.setPassword(password);

        return taskDto;
    }
}

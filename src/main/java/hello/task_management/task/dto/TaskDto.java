package hello.task_management.task.dto;

import hello.task_management.task.dto.request.CreateTaskDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id; // task_id

    private String task; // 할일 내용
    private long authorId; // users 테이블에 등록된 user_id
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

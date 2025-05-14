package hello.task_management.task.dto.response;

import hello.task_management.task.dto.TaskDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class TaskResponseDto {
    private Long id;
    private String task;
    private Long authorId;
    private String authorName;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static TaskResponseDto fromTaskDto(TaskDto taskDto) {
        TaskResponseDto taskResponseDto = new TaskResponseDto();

        taskResponseDto.setId(taskDto.getId());
        taskResponseDto.setTask(taskDto.getTask());
        taskResponseDto.setAuthorId(taskDto.getAuthorId());
        taskResponseDto.setAuthorName(taskDto.getAuthorName());
        taskResponseDto.setCreatedAt(taskDto.getCreatedAt());
        taskResponseDto.setLastModifiedAt(taskDto.getLastModifiedAt());

        return taskResponseDto;
    }
}

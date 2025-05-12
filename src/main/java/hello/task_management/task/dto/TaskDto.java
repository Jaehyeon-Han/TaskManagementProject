package hello.task_management.task.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    private Long id;

    private String task;
    private String author;

    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}

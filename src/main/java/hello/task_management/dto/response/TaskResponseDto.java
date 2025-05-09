package hello.task_management.dto.response;

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
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}

package hello.task_management.user.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private long userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}

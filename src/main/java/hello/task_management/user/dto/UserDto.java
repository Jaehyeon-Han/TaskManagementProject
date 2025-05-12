package hello.task_management.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String name;
    private String email;

    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}

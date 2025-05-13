package hello.task_management.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateUserDto {
    private String name;

    @Email
    private String email;
    @NotBlank
    private String password;

    public boolean isEmpty() {
        return name == null && email == null;
    }
}

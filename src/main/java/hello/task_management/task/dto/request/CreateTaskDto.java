package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {
    @NotBlank
    private String task;
    @NotNull
    private Long authorId;
    @NotBlank
    private String authorPassword;
    @NotBlank
    private String password;
}

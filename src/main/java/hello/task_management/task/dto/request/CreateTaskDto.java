package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {
    @NotBlank
    private String task;
    @NotBlank
    private String author;
    @NotBlank
    private String password;
}

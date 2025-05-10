package hello.task_management.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DeleteTaskDto {
    @NotEmpty
    private String password;
}

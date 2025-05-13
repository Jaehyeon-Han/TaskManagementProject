package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DeleteTaskDto {
    @NotEmpty
    private String taskPassword;
}

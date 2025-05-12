package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDto {
    private String task;
    @NotBlank
    private String password;

    public boolean isEmpty() {
        return task == null;
    }
}

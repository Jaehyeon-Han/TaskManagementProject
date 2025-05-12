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
    private String author;

    public boolean isEmpty() {
        return task == null && author == null;
    }
}

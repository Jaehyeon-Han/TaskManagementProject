package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDto {

    // 다른 필드도 수정가능하게 될 가능성이 있다고 생각하여
    // @NotNull을 사용하지 않음
    private String task;
    @NotBlank
    private String taskPassword;

    public boolean isEmpty() {
        return task == null;
    }
}

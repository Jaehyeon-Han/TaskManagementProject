package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDto {

    @NotBlank
    @Size(max = 200)
    private String task;
    @NotNull
    private Long authorId;
    @NotBlank
    private String authorPassword; // user 테이블에 등록된 비밀번호
    @NotBlank
    private String taskPassword; // 할일의 비밀번호
}

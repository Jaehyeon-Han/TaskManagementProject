package hello.task_management.task.dto.request;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DeleteTaskDto {
    @NotEmpty
    private String taskPassword; // 할일 생성 시 등록한 비밀번호
}

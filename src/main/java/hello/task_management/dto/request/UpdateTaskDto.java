package hello.task_management.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDto {
    private String task;
    private String password;
    private String author;
}

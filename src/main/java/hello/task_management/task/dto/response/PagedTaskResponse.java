package hello.task_management.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedTaskResponse {
    private List<TaskResponseDto> content; // 페이징된 할일 목록
    private int page; // 현재 페이지
    private int size; // 페이지당 할일 수
    private long totalElements; // 전체 할일 수
    private int totalPages; // 전체 페이지 수
}

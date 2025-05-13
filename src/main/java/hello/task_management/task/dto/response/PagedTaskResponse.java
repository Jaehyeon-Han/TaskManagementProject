package hello.task_management.task.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PagedTaskResponse {
    private List<TaskResponseDto> content;   // 페이징된 데이터 리스트
    private int page;                // 현재 페이지
    private int size;                // 페이지당 데이터 수
    private long totalElements;      // 전체 데이터 수
    private int totalPages;          // 전체 페이지 수
}

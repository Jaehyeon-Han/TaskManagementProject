package hello.task_management.task.controller;

import hello.task_management.global.error.exception.TaskNotFoundException;
import hello.task_management.global.error.exception.UserNotFoundException;
import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.DeleteTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import hello.task_management.task.dto.response.PagedTaskResponse;
import hello.task_management.task.dto.response.TaskResponseDto;
import hello.task_management.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {

    // TaskController에서 유저를 찾지 못하는 경우는
    // 잘못된 task_id를 입력한 경우이기 때문에 NOT_FOUND가 아닌 BAD_REQUEST를 던진다
    @ExceptionHandler
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    private final TaskService taskService;

    // /pages 경로로 별도로 매핑할 수 있지만
    // URI는 리소스를 식별해야 한다는 원칙을 따라 /tasks에 매핑한다
    @GetMapping
    public ResponseEntity<PagedTaskResponse> findAllTasks(@RequestParam(required = false) String author,
                                                          @RequestParam(required = false) LocalDate lastModifiedDate,
                                                          @RequestParam(defaultValue = "0") int page,
                                                          @RequestParam(defaultValue = "1") int pageSize
    ) {
        return ResponseEntity.ok(taskService.findAllTasks(author, lastModifiedDate, page, pageSize));
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid CreateTaskDto createTaskDto) {
        TaskResponseDto createdTask = taskService.createTask(createTaskDto);

        // 201 응답은 location 헤더를 제공해야 한다
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId()).toUri();

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> findTaskById(@PathVariable long taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> updateTaskById(@PathVariable long taskId, @RequestBody @Valid UpdateTaskDto updateTaskDto) {
        // 별도의 예외로 만들어도 좋을 것 같다
        if(updateTaskDto.isEmpty()) {
            return ResponseEntity.badRequest().body("업데이트할 필드가 없습니다.");
        }

        TaskResponseDto taskResponseDto = taskService.updateTaskById(taskId, updateTaskDto);

        return ResponseEntity.ok(taskResponseDto);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTaskById(@PathVariable long taskId, @Valid @RequestBody DeleteTaskDto deleteTaskDto) {
        taskService.deleteTaskById(taskId, deleteTaskDto);

        return ResponseEntity.noContent().build();
    }
}

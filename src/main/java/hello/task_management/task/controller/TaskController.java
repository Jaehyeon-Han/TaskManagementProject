package hello.task_management.task.controller;

import hello.task_management.task.dto.request.CreateTaskDto;
import hello.task_management.task.dto.request.DeleteTaskDto;
import hello.task_management.task.dto.request.UpdateTaskDto;
import hello.task_management.task.dto.response.TaskResponseDto;
import hello.task_management.global.error.exception.PasswordMismatchException;
import hello.task_management.global.error.exception.TaskNotFoundException;
import hello.task_management.task.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @ExceptionHandler
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @PostMapping
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody @Valid CreateTaskDto createTaskDto) {
        TaskResponseDto createdTask = taskService.createTask(createTaskDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTask.getId()).toUri();

        return ResponseEntity.created(location).body(createdTask);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> findAllTasks(@RequestParam(required = false) String author,
                                                              @RequestParam(required = false) LocalDate lastModifiedDate) {
        return ResponseEntity.ok(taskService.findAllTasks(author, lastModifiedDate));
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> findTaskById(@PathVariable long taskId) {
        return ResponseEntity.ok(taskService.findTaskById(taskId));
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<?> updateTaskById(@PathVariable long taskId, @RequestBody @Valid UpdateTaskDto updateTaskDto) {
        if(updateTaskDto.isEmpty()) {
            return ResponseEntity.badRequest().body("업데이트할 필드가 없습니다.");
        }

        TaskResponseDto taskResponseDto = taskService.updateTaskById(taskId, updateTaskDto);

        return ResponseEntity.ok(taskResponseDto);
    }

    @DeleteMapping("/{taskId}")
    @Transactional
    public ResponseEntity<Void> deleteTaskById(@PathVariable long taskId, @Valid @RequestBody DeleteTaskDto deleteTaskDto) {
        taskService.deleteTaskById(taskId, deleteTaskDto);

        return ResponseEntity.noContent().build();
    }
}

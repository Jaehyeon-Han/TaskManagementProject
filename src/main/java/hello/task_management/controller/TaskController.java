package hello.task_management.controller;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.DeleteTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.exception.PasswordMismatchException;
import hello.task_management.exception.TaskNotFoundException;
import hello.task_management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
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
    public ResponseEntity<TaskResponseDto> updateTaskById(@PathVariable long taskId, @Valid @RequestBody UpdateTaskDto updateTaskDto) {
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

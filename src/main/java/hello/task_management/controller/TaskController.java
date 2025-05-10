package hello.task_management.controller;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.exception.PasswordMismatchException;
import hello.task_management.exception.TaskNotFoundException;
import hello.task_management.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
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
    public ResponseEntity<List<TaskResponseDto>> findAllTasks() {
        return new ResponseEntity<>(taskService.findAllTasks(), HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> findTaskById(@PathVariable long taskId) {
        return new ResponseEntity<>(taskService.findTaskById(taskId), HttpStatus.OK);
    }

    @PatchMapping("/{taskId}")
    public ResponseEntity<TaskResponseDto> updateTaskById(@PathVariable long taskId, @Valid @RequestBody UpdateTaskDto updateTaskDto) {
        TaskResponseDto taskResponseDto = taskService.updateTaskById(taskId, updateTaskDto);

        return new ResponseEntity<>(taskResponseDto, HttpStatus.OK);
    }
}

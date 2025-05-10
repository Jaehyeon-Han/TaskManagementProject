package hello.task_management.service;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.request.UpdateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
import hello.task_management.exception.PasswordMismatchException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Sql(scripts = "classpath:schema.sql")
class TaskServiceImplTest {
    @Autowired
    TaskService taskService;

    @Test
    void createTask() {
        // given
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTask("task");
        createTaskDto.setAuthor("author");
        createTaskDto.setPassword("password");

        // when
        TaskResponseDto createdTaskResponseDto = taskService.createTask(createTaskDto);

        // then
        assertEquals(1, createdTaskResponseDto.getId());
        assertEquals("task", createdTaskResponseDto.getTask());
        assertEquals("author", createdTaskResponseDto.getAuthor());
        System.out.println("Created at: " + createdTaskResponseDto.getCreatedAt());
        System.out.println("Last modified at: " + createdTaskResponseDto.getLastModifiedAt());
    }

    @Test
    void updateTaskByIdSuccess() {
        // given
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTask("task");
        createTaskDto.setAuthor("author");
        createTaskDto.setPassword("password");
        TaskResponseDto taskResponseDto = taskService.createTask(createTaskDto);

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTask("modified task");
        updateTaskDto.setPassword("password");

        // when
        try {
            Thread.sleep(1000); // delay for last modified time
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        long taskId = taskResponseDto.getId();
        taskService.updateTaskById(taskId, updateTaskDto);

        // then
        TaskResponseDto modifiedTask = taskService.findTaskById(taskId);
        assertEquals("modified task", modifiedTask.getTask());
        assertNotEquals(modifiedTask.getCreatedAt(), modifiedTask.getLastModifiedAt());
    }

    @Test
    void updateTaskByIdFail() {
        // given
        CreateTaskDto createTaskDto = new CreateTaskDto();
        createTaskDto.setTask("task");
        createTaskDto.setAuthor("author");
        createTaskDto.setPassword("password");
        TaskResponseDto taskResponseDto = taskService.createTask(createTaskDto);

        UpdateTaskDto updateTaskDto = new UpdateTaskDto();
        updateTaskDto.setTask("modified task");
        updateTaskDto.setPassword("invalid password");

        // when + then
        long taskId = taskResponseDto.getId();
        assertThrows(PasswordMismatchException.class, () -> taskService.updateTaskById(taskId, updateTaskDto));
    }
}
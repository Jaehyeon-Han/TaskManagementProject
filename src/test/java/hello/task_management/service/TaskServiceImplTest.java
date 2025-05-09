package hello.task_management.service;

import hello.task_management.dto.request.CreateTaskDto;
import hello.task_management.dto.response.TaskResponseDto;
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
}
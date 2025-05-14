package hello.task_management.task.repository;

import hello.task_management.task.dto.TaskDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskRepositoryImplTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void withoutUsingColumns() {
        TaskDto taskDto = new TaskDto(null, "task", 1, "god", "password", null, null);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("tasks")
                .usingGeneratedKeyColumns("task_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", taskDto.getTask());
        parameters.put("author_id", taskDto.getAuthorId());
        parameters.put("password", taskDto.getPassword());

        simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Test
    void withUsingColumns() {
        TaskDto taskDto = new TaskDto(null, "task2", 1, "god", "password", null, null);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("tasks")
                .usingColumns("task", "author_id", "password")
                .usingGeneratedKeyColumns("task_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", taskDto.getTask());
        parameters.put("author_id", taskDto.getAuthorId());
        parameters.put("password", taskDto.getPassword());

        simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Test
    void withWrongParameterName() {
        TaskDto taskDto = new TaskDto(null, "task", 1, "god", "password", null, null);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("tasks")
                .usingGeneratedKeyColumns("task_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", taskDto.getTask());
        parameters.put("author_ids", taskDto.getAuthorId()); // wrong parameter name
        parameters.put("password", taskDto.getPassword());

        simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }
}
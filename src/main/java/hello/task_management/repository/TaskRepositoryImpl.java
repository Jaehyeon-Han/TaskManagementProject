package hello.task_management.repository;

import hello.task_management.dto.TaskDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class TaskRepositoryImpl implements TaskRepository {
    private final JdbcTemplate jdbcTemplate;

    public TaskRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long createTask(TaskDto taskDto) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("tasks")
                .usingColumns("task", "author", "password")
                .usingGeneratedKeyColumns("task_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", taskDto.getTask());
        parameters.put("author", taskDto.getAuthor());
        parameters.put("password", taskDto.getPassword());

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Optional<TaskDto> findTaskById(long createdTaskId) {
        String sql = "SELECT task_id, task, author, password, created_at, last_modified_at FROM tasks WHERE task_id = ?";

        List<TaskDto> result = jdbcTemplate.query(sql, taskDtoRowMapper(), createdTaskId);
        return result.stream().findAny();
    }

    @Override
    public List<TaskDto> findAllTasks() {
        String sql = "SELECT task_id, task, author, password, created_at, last_modified_at FROM tasks ORDER BY last_modified_at DESC";

        return jdbcTemplate.query(sql, taskDtoRowMapper());
    }

    @Override
    public int updateTask(TaskDto taskDto) {
       String sql = "UPDATE tasks SET task = ?, author = ? WHERE task_id = ?";

       return jdbcTemplate.update(sql, taskDto.getTask(), taskDto.getAuthor(), taskDto.getId());
    }

    @Override
    public void deleteTaskById(long taskId) {
        String sql = "DELETE FROM tasks WHERE task_id = ?";

        jdbcTemplate.update(sql, taskId);
    }

    private RowMapper<TaskDto> taskDtoRowMapper() {
        return (rs, rowNum) -> {
            return new TaskDto(
                    rs.getLong("task_id"),
                    rs.getString("task"),
                    rs.getString("author"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("last_modified_at").toLocalDateTime()
            );
        };
    }
}

package hello.task_management.task.repository;

import hello.task_management.task.dto.TaskDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.*;

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
                .usingColumns("task", "author_id", "password")
                .usingGeneratedKeyColumns("task_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("task", taskDto.getTask());
        parameters.put("author_id", taskDto.getAuthorId());
        parameters.put("password", taskDto.getPassword());

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public Optional<TaskDto> findTaskById(long createdTaskId) {
        String sql = """
                SELECT t.task_id, t.task, t.author_id, u.name as author_name, t.password, t.created_at, t.last_modified_at
                FROM tasks t
                LEFT JOIN users u ON t.author_id = u.user_id
                WHERE task_id = ?
                """;

        List<TaskDto> result = jdbcTemplate.query(sql, taskDtoRowMapper(), createdTaskId);
        return result.stream().findAny();
    }

    @Override
    public List<TaskDto> findAllTasks(String authorName, LocalDate lastModifiedDate) {
        StringBuilder sql = new StringBuilder(
                """
                SELECT task_id, task, u.user_id, u.name AS author_name, password, created_at, last_modified_at
                FROM tasks t
                LEFT JOIN users u ON t.author_id = u.user_id
                WHERE 1=1
                """
        );

        List<Object> params = new ArrayList<>();

        if (authorName != null) {
            sql.append(" AND u.name = ?");
            params.add(authorName);
        }

        if (lastModifiedDate != null) {
            sql.append(" AND DATE(t.last_modified_at) = ?");
            params.add(lastModifiedDate);
        }

        sql.append(" ORDER BY t.last_modified_at DESC");

        return jdbcTemplate.query(sql.toString(), params.toArray(), taskDtoRowMapper());
    }


    @Override
    public int updateTask(TaskDto taskDto) {
        String sql = "UPDATE tasks SET task = ? WHERE task_id = ?";

        return jdbcTemplate.update(sql, taskDto.getTask(), taskDto.getId());
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
                    rs.getLong("author_id"),
                    rs.getString("author_name"),
                    rs.getString("password"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("last_modified_at").toLocalDateTime()
            );
        };
    }
}

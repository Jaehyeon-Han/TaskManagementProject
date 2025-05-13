package hello.task_management.user.repository;

import hello.task_management.task.dto.TaskDto;
import hello.task_management.user.dto.UserDto;
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
public class UserRepositoryImpl implements UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public long createUser(UserDto userDto) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("users")
                .usingColumns("name", "email", "password")
                .usingGeneratedKeyColumns("user_id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", userDto.getName());
        parameters.put("email", userDto.getEmail());
        parameters.put("password", userDto.getPassword());

        return simpleJdbcInsert.executeAndReturnKey(parameters).longValue();
    }

    @Override
    public List<UserDto> findAllUsers() {
        String sql = "SELECT user_id, name, email, password, created_at, last_modified_at FROM users";

        return jdbcTemplate.query(sql, userDtoRowMapper());
    }

    @Override
    public int updateUser(UserDto userDto) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE user_id = ?";

        return jdbcTemplate.update(sql, userDto.getName(), userDto.getEmail(), userDto.getId());
    }

    @Override
    public Optional<UserDto> findUserById(long userId) {
        String sql = "SELECT user_id, name, email, password, created_at, last_modified_at FROM users WHERE user_id = ?";

        return jdbcTemplate.query(sql, userDtoRowMapper(), userId).stream().findAny();
    }

    private RowMapper<UserDto> userDtoRowMapper() {
        return (rs, rowNum) -> new UserDto(
                rs.getLong("user_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getTimestamp("created_at").toLocalDateTime(),
                rs.getTimestamp("last_modified_at").toLocalDateTime()
        );
    }
}

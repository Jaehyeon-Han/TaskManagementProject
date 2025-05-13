package hello.task_management.user.repository;

import hello.task_management.user.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    long createUser(UserDto userDto);

    List<UserDto> findAllUsers();

    int updateUser(UserDto userDto);

    Optional<UserDto> findUserById(long userId);
}

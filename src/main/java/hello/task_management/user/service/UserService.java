package hello.task_management.user.service;

import hello.task_management.user.dto.request.CreateUserDto;
import hello.task_management.user.dto.request.UpdateUserDto;
import hello.task_management.user.dto.response.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(CreateUserDto createUserDto);

    List<UserResponseDto> findAllUsers();

    UserResponseDto updateUserById(long id, UpdateUserDto updateUserDto);
}

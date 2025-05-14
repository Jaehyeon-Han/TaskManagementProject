package hello.task_management.user.service;

import hello.task_management.global.error.exception.UserNotFoundException;
import hello.task_management.user.dto.UserDto;
import hello.task_management.user.dto.request.CreateUserDto;
import hello.task_management.user.dto.request.UpdateUserDto;
import hello.task_management.user.dto.response.UserResponseDto;
import hello.task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static hello.task_management.global.validation.PasswordMatcher.checkPasswordMatchOrThrowPasswordMismatch;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(CreateUserDto createUserDto) {
        UserDto userToCreate = UserDto.fromCreateUserDto(createUserDto);
        long createdUserId = userRepository.createUser(userToCreate);

        UserDto createdUser = findByIdOrThrowUserNotFound(createdUserId);
        return mapUserDtoToUserResponseDto(createdUser);
    }

    @Override
    public List<UserResponseDto> findAllUsers() {
        return userRepository.findAllUsers().stream().map(UserResponseDto::fromUserDto).toList();
    }

    @Override
    public UserResponseDto updateUserById(long userId, UpdateUserDto updateUserDto) {
        UserDto userDto = findByIdOrThrowUserNotFound(userId);

        checkPasswordMatchOrThrowPasswordMismatch(updateUserDto.getPassword(), userDto.getPassword());

        String modifiedName = updateUserDto.getName();
        if(modifiedName != null) {
            userDto.setName(modifiedName);
        }

        String modifiedEmail = updateUserDto.getEmail();
        if(modifiedEmail != null) {
            userDto.setEmail(modifiedEmail);
        }

        userRepository.updateUser(userDto);

        UserDto updatedUser = findByIdOrThrowUserNotFound(userId);
        return mapUserDtoToUserResponseDto(updatedUser);
    }

    private UserDto findByIdOrThrowUserNotFound(long userId) {
        return userRepository.findUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User for id " + userId + " does not exist"));
    }

    private UserResponseDto mapUserDtoToUserResponseDto(UserDto userDto) {
        return UserResponseDto.fromUserDto(userDto);
    }
}

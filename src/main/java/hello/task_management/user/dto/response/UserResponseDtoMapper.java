package hello.task_management.user.dto.response;

import hello.task_management.user.dto.UserDto;

public class UserResponseDtoMapper {
    public static UserResponseDto fromUserDto(UserDto userDto) {
        UserResponseDto userResponseDto = new UserResponseDto();

        userResponseDto.setUserId(userDto.getId());
        userResponseDto.setName(userDto.getName());
        userResponseDto.setEmail(userDto.getEmail());
        userResponseDto.setCreatedAt(userDto.getCreatedAt());
        userResponseDto.setLastModifiedAt(userDto.getLastModifiedAt());

        return userResponseDto;
    }
}

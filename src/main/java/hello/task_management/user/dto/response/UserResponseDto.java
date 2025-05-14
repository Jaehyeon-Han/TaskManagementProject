package hello.task_management.user.dto.response;

import hello.task_management.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponseDto {
    private long userId;
    private String name;
    private String email;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

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

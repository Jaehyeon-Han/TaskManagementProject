package hello.task_management.user.dto;

import hello.task_management.user.dto.request.CreateUserDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String name;
    private String email;

    private String password;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    public static UserDto fromCreateUserDto(CreateUserDto createUserDto) {
        UserDto userDto = new UserDto();

        userDto.setName(createUserDto.getName());
        userDto.setEmail(createUserDto.getEmail());
        userDto.setPassword(createUserDto.getPassword());

        return userDto;
    }
}

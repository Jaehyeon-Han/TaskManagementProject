package hello.task_management.user.dto;

import hello.task_management.user.dto.request.CreateUserDto;

public class UserDtoMapper {
    public static UserDto fromCreateUserDto(CreateUserDto createUserDto) {
        UserDto userDto = new UserDto();

        userDto.setName(createUserDto.getName());
        userDto.setEmail(createUserDto.getEmail());
        userDto.setPassword(createUserDto.getPassword());

        return userDto;
    }
}

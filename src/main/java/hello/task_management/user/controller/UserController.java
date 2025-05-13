package hello.task_management.user.controller;

import hello.task_management.user.dto.request.CreateUserDto;
import hello.task_management.user.dto.request.UpdateUserDto;
import hello.task_management.user.dto.response.UserResponseDto;
import hello.task_management.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> findAllUsers() {
        List<UserResponseDto> allUsers = userService.findAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody @Valid CreateUserDto createUserDto) {
        UserResponseDto createdUser = userService.createUser(createUserDto);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{user_id}")
                .buildAndExpand(createdUser.getUserId()).toUri();

        return ResponseEntity.created(location).body(createdUser);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable long userId, @RequestBody @Valid UpdateUserDto updateUserDto) {
        if (updateUserDto.isEmpty()) {
            return ResponseEntity.badRequest().body("업데이트할 필드가 없습니다.");
        }

        UserResponseDto updatedUser = userService.updateUserById(userId, updateUserDto);

        return ResponseEntity.ok(updatedUser);
    }
}
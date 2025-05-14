package hello.task_management.user.service;

import hello.task_management.global.error.exception.UserNotFoundException;
import hello.task_management.global.error.exception.UserPasswordMismatchException;
import hello.task_management.user.dto.UserDto;
import hello.task_management.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserAuthenticationService {

    private final UserRepository userRepository;

    public void authenticateUserOrThrowUserExceptions(long authorId, String authorPassword) {
        Optional<UserDto> optionalFoundUser = userRepository.findUserById(authorId);

        if(optionalFoundUser.isEmpty()) {
            throw new UserNotFoundException("User for id : " + authorId + "does not exist");
        }

        if(!authorPassword.equals(optionalFoundUser.get().getPassword())) {
            throw new UserPasswordMismatchException("user password does not match");
        }
    }
}

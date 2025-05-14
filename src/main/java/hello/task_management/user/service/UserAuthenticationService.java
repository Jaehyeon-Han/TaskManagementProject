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

    // 사용자를 찾아서 비밀번호를 검증하는 역할만 수행한다
    // UserService에 넣어도 되지만 기존 구현이 UserResponseDto를 반환하도록 구현하였기에
    // 수정하지 않기 위해 새로 클래스를 생성하였다
    public UserDto authenticateUserOrThrowUserExceptions(long authorId, String authorPassword) {
        Optional<UserDto> optionalFoundUser = userRepository.findUserById(authorId);

        if(optionalFoundUser.isEmpty()) {
            throw new UserNotFoundException("User for id : " + authorId + " does not exist");
        }

        if(!authorPassword.equals(optionalFoundUser.get().getPassword())) {
            throw new UserPasswordMismatchException("user password does not match");
        }

        return optionalFoundUser.get();
    }
}

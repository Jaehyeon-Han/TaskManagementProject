package hello.task_management.global.validation;

import hello.task_management.global.error.exception.TaskPasswordMismatchException;

// 도메인 별로 비밀번호 매칭 로직이 달라질 수 있으므로
// 하나의 유틸 클래스로 쓰는 것은 권장되지 않을 것 같다
public class PasswordMatcher {
    public static void checkPasswordMatchOrThrowPasswordMismatch(String inputPassword, String storedPassword) {
        if(!inputPassword.equals(storedPassword)) {
            throw new TaskPasswordMismatchException("password does not match");
        }
    }
}

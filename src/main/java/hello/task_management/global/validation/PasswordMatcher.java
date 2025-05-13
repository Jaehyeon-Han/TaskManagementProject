package hello.task_management.global.validation;

import hello.task_management.global.error.exception.TaskPasswordMismatchException;

public class PasswordMatcher {
    public static void checkPasswordMatchOrThrowPasswordMismatch(String inputPassword, String storedPassword) {
        if(!inputPassword.equals(storedPassword)) {
            throw new TaskPasswordMismatchException("password does not match");
        }
    }
}

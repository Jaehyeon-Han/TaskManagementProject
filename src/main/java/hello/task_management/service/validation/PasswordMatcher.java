package hello.task_management.service.validation;

import hello.task_management.exception.PasswordMismatchException;

public class PasswordMatcher {
    public static void checkPasswordMatchOrThrowPasswordMismatch(String inputPassword, String storedPassword) {
        if(!inputPassword.equals(storedPassword)) {
            throw new PasswordMismatchException("password does not match");
        }
    }
}

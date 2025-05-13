package hello.task_management.global.error.exception;

public class UserPasswordMismatchException extends RuntimeException {
    public UserPasswordMismatchException(String message) {
        super(message);
    }
}

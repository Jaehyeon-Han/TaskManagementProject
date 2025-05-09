package hello.task_management.exception;

public class RequiredFieldEmptyException extends RuntimeException {
    public RequiredFieldEmptyException(String message) {
        super(message);
    }
}

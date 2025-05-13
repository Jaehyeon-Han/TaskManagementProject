package hello.task_management.global.error.exception;

public class TaskPasswordMismatchException extends RuntimeException {
    public TaskPasswordMismatchException(String message) {
        super(message);
    }
}

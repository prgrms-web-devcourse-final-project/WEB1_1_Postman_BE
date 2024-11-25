package postman.bottler.notification.exception;

public class NoLetterIdException extends RuntimeException {
    private static final String MESSAGE = "편지 관련 알림은 편지 ID가 있어야 합니다.";

    public NoLetterIdException() {
        super(MESSAGE);
    }
}

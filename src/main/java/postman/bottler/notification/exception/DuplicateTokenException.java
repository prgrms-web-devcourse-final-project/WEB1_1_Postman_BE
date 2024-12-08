package postman.bottler.notification.exception;

public class DuplicateTokenException extends RuntimeException {
    private static final String MESSAGE = "해당 기기는 이미 알림이 허용되어 있습니다.";

    public DuplicateTokenException() {
        super(MESSAGE);
    }
}

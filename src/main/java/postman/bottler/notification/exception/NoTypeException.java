package postman.bottler.notification.exception;

public class NoTypeException extends RuntimeException {
    private static final String MESSAGE = "해당하는 타입의 알림 유형이 존재하지 않습니다.";

    public NoTypeException() {
        super(MESSAGE);
    }
}

package postman.bottler.notification.exception;

public class NeedLabelException extends RuntimeException {
    private final static String MESSAGE = "라벨 이미지가 필요한 알림입니다.";

    public NeedLabelException() {
        super(MESSAGE);
    }
}

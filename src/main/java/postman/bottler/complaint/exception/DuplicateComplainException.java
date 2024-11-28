package postman.bottler.complaint.exception;

public class DuplicateComplainException extends RuntimeException {
    private static final String MESSAGE = "이미 신고된 편지입니다.";

    public DuplicateComplainException() {
        super(MESSAGE);
    }
}

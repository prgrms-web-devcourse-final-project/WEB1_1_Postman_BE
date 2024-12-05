package postman.bottler.slack;

public enum SlackConstant {
    WARNING("⚠️ [사용자 경고 알림] 사용자 ID: %d의 경고가 증가했습니다. 계정 상태를 확인해 주세요."),
    BAN("🚨 [사용자 정지 알림] 사용자 ID: %d의 계정이 정지되었습니다. 계정 상태를 확인해 주세요.");

    private final String message;

    SlackConstant(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

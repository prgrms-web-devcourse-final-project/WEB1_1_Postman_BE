package postman.bottler.notification.domain;

import lombok.Getter;
import postman.bottler.notification.exception.NoTypeException;

@Getter
public enum NotificationType {
    NEW_LETTER("새 편지 도착", "새로운 키워드 편지가 도착했어요."),
    TARGET_LETTER("새 편지 도착", "나에게 작성된 지도 편지가 있어요."),
    REPLY_LETTER("새 편지 도착", "내가 작성한 편지의 답장이 도착했어요."),
    WARNING("경고 안내", "신고가 누적된 편지가 존재합니다."),
    BAN("정지 안내", "경고 누적으로 당분간 편지 작성이 제한됩니다.");

    private final String title;
    private final String content;

    NotificationType(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Boolean isLetterNotification() {
        return this == NotificationType.NEW_LETTER ||
                this == NotificationType.TARGET_LETTER ||
                this == NotificationType.REPLY_LETTER;
    }

    public static NotificationType from(String type) {
        for (NotificationType value : NotificationType.values()) {
            if (value.name().equals(type)) {
                return value;
            }
        }
        throw new NoTypeException();
    }
}

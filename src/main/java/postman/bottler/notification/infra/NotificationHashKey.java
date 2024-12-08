package postman.bottler.notification.infra;

import lombok.Getter;

@Getter
public enum NotificationHashKey {
    NOTIFICATION_KEY("notification"),
    ID("id"),
    TYPE("type"),
    RECEIVER("receiver"),
    CREATED_AT("created_at"),
    LETTER_ID("letter_id"),
    IS_READ("is_read"),
    LABEL("label");

    private final String key;

    NotificationHashKey(String key) {
        this.key = key;
    }
}

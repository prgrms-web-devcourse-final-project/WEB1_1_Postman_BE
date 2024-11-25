package postman.bottler.notification.domain;


public enum NotificationType {
    NEW_LETTER,
    TARGET_LETTER,
    REPLY_LETTER,
    WARNING,
    BAN;

    public Boolean isLetterNotification() {
        return this == NotificationType.NEW_LETTER ||
                this == NotificationType.TARGET_LETTER ||
                this == NotificationType.REPLY_LETTER;
    }
}

package postman.bottler.notification.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import postman.bottler.notification.exception.InvalidNotificationRequestException;
import postman.bottler.notification.exception.NoTypeException;

@Getter
public class Notification {
    private Long id;

    private final NotificationType type;

    private final long receiver;

    private LocalDateTime createdAt;

    private Boolean isRead;

    protected Notification(NotificationType type, Long receiver, Boolean isRead) {
        this.type = type;
        this.receiver = receiver;
        this.createdAt = LocalDateTime.now();
        this.isRead = isRead;
    }

    protected Notification(Long id, NotificationType type, Long receiver, Boolean isRead) {
        this.id = id;
        this.type = type;
        this.receiver = receiver;
        this.createdAt = LocalDateTime.now();
        this.isRead = isRead;
    }

    public static Notification create(String type, Long receiver, Long letterId) {
        validateType(type);
        NotificationType notificationType = NotificationType.valueOf(type);
        if (notificationType.isLetterNotification()) {
            return new LetterNotification(notificationType, receiver, letterId, false);
        }
        return new Notification(notificationType, receiver, false);
    }

    public static Notification of(Long id, NotificationType type, Long receiver, Long letterId, Boolean isRead) {
        if (type.isLetterNotification()) {
            return new LetterNotification(id, type, receiver, letterId, isRead);
        }
        return new Notification(id, type, receiver, isRead);
    }

    private static void validateType(String type) {
        for (NotificationType value : NotificationType.values()) {
            if (value.name().equals(type)) {
                return;
            }
        }
        throw new NoTypeException();
    }

    public void read() {
        this.isRead = true;
    }
}

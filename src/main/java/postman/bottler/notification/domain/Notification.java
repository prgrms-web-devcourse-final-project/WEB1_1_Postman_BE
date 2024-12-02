package postman.bottler.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;

@Getter
public class Notification {
    private final UUID id;

    private final NotificationType type;

    private final long receiver;

    private final LocalDateTime createdAt;

    private Boolean isRead;

    protected Notification(NotificationType type, Long receiver, Boolean isRead) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.receiver = receiver;
        this.createdAt = LocalDateTime.now();
        this.isRead = isRead;
    }

    protected Notification(UUID id, NotificationType type, Long receiver, LocalDateTime createdAt, Boolean isRead) {
        this.id = id;
        this.type = type;
        this.receiver = receiver;
        this.createdAt = createdAt;
        this.isRead = isRead;
    }

    public static Notification create(String type, Long receiver, Long letterId) {
        NotificationType notificationType = NotificationType.from(type);
        if (notificationType.isLetterNotification()) {
            return new LetterNotification(notificationType, receiver, letterId, false);
        }
        return new Notification(notificationType, receiver, false);
    }

    public static Notification of(UUID id, NotificationType type, Long receiver,
                                  Long letterId, LocalDateTime createdAt, Boolean isRead) {
        if (type.isLetterNotification()) {
            return new LetterNotification(id, type, receiver, letterId, createdAt, isRead);
        }
        return new Notification(id, type, receiver, createdAt, isRead);
    }

    public void read() {
        this.isRead = true;
    }
}

package postman.bottler.notification.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import postman.bottler.notification.exception.InvalidNotificationRequestException;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Notification {
    private final NotificationType type;

    private final long receiver;

    private LocalDateTime createdAt;

    protected Notification(NotificationType type, Long receiver) {
        this.type = type;
        this.receiver = receiver;
        this.createdAt = LocalDateTime.now();
    }

    public static Notification of(String type, Long receiver, Long letterId) {
        validateType(type);
        NotificationType notificationType = NotificationType.valueOf(type);
        if (notificationType.isLetterNotification()) {
            return new LetterNotification(notificationType, receiver, letterId);
        }
        return new Notification(notificationType, receiver);
    }

    private static void validateType(String type) {
        for (NotificationType value : NotificationType.values()) {
            if (value.name().equals(type)) {
                return;
            }
        }
        throw new InvalidNotificationRequestException("해당하는 타입의 알림 유형이 존재하지 않습니다.");
    }
}

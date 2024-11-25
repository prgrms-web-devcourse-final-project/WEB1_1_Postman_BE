package postman.bottler.notification.dto.response;

import java.time.LocalDateTime;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;

public record NotificationResponseDTO(
        Long id,
        NotificationType type,
        Long receiver,
        LocalDateTime createdAt,
        Long letterId,
        Boolean isRead
) {
    public static NotificationResponseDTO from(final Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getType(),
                notification.getReceiver(),
                notification.getCreatedAt(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification.getIsRead());
    }
}

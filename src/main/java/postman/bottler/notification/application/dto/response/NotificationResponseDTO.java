package postman.bottler.notification.application.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;

public record NotificationResponseDTO(
        UUID id,
        NotificationType type,
        Long receiver,
        LocalDateTime createdAt,
        Long letterId,
        Boolean isRead,
        String label
) {
    public static NotificationResponseDTO from(final Notification notification) {
        return new NotificationResponseDTO(
                notification.getId(),
                notification.getType(),
                notification.getReceiver(),
                notification.getCreatedAt(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLetterId() : null,
                notification.getIsRead(),
                notification instanceof LetterNotification ? ((LetterNotification) notification).getLabel() : null);
    }
}

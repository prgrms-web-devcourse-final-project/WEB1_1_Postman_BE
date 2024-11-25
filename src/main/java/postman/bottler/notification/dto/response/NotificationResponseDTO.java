package postman.bottler.notification.dto.response;

import java.time.LocalDateTime;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;

public record NotificationResponseDTO(
        NotificationType type,
        Long receiver,
        LocalDateTime createdAt
) {
    public static NotificationResponseDTO from(final Notification notification) {
        return new NotificationResponseDTO(notification.getType(), notification.getReceiver(),
                notification.getCreatedAt());
    }
}

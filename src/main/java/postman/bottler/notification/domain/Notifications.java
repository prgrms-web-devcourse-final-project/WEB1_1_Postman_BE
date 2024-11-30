package postman.bottler.notification.domain;

import java.util.Comparator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@Builder
@Getter
public class Notifications {
    private List<Notification> notifications;

    public static Notifications from(List<Notification> notifications) {
        return Notifications.builder()
                .notifications(notifications)
                .build();
    }

    public void orderByCreatedAt() {
        notifications.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
    }

    public void markAsRead() {
        for (Notification notification : notifications) {
            if (!notification.getIsRead()) {
                notification.read();
            }
        }
    }

    public List<NotificationResponseDTO> createDTO() {
        return notifications.stream()
                .map(NotificationResponseDTO::from)
                .toList();
    }
}

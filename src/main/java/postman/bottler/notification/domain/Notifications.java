package postman.bottler.notification.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

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

    public Notifications markAsRead() {
        List<Notification> changed = new ArrayList<>();
        for (Notification notification : notifications) {
            if (!notification.getIsRead()) {
                changed.add(notification.read());
            }
        }
        return new Notifications(changed);
    }
}

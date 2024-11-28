package postman.bottler.notification.service;

import java.util.List;
import postman.bottler.notification.domain.Notification;

public interface NotificationRepository {
    Notification save(Notification notification);

    List<Notification> findByReceiver(Long userId);

    void updateNotifications(List<Notification> notification);
}

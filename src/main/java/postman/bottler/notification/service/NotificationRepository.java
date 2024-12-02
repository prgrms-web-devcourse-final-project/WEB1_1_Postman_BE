package postman.bottler.notification.service;

import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.Notifications;

public interface NotificationRepository {
    Notification save(Notification notification);

    Notifications findByReceiver(Long userId);

    void updateNotifications(Notifications notifications);
}

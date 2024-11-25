package postman.bottler.notification.service;

import postman.bottler.notification.domain.Notification;

public interface NotificationRepository {
    Notification save(Notification notification);
}

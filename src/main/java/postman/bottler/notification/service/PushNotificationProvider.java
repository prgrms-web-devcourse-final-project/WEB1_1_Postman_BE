package postman.bottler.notification.service;

import postman.bottler.notification.domain.PushMessage;

public interface PushNotificationProvider {
    public void push(PushMessage pushMessage);
}

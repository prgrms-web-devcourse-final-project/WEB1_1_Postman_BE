package postman.bottler.notification.service;

import postman.bottler.notification.domain.PushMessages;

public interface PushNotificationProvider {
    public void pushAll(PushMessages pushMessages);
}

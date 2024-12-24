package postman.bottler.notification.application;

import postman.bottler.notification.domain.PushMessages;

public interface PushNotificationProvider {
    public void pushAll(PushMessages pushMessages);
}

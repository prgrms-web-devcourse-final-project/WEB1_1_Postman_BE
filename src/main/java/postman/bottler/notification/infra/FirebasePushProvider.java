package postman.bottler.notification.infra;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import postman.bottler.notification.domain.PushMessage;
import postman.bottler.notification.service.PushNotificationProvider;

@Slf4j
@Component
public class FirebasePushProvider implements PushNotificationProvider {
    @Override
    public void push(PushMessage pushMessage) {
        Message firebaseMessage = Message.builder()
                .setToken(pushMessage.getToken())
                .setNotification(Notification.builder()
                        .setTitle(pushMessage.getTitle())
                        .setBody(pushMessage.getContent())
                        .build())
                .build();
        try {
            FirebaseMessaging.getInstance().send(firebaseMessage);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}

package postman.bottler.notification.infra;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.service.PushNotificationProvider;

@Slf4j
@Component
@RequiredArgsConstructor
public class FirebasePushProvider implements PushNotificationProvider {
    private final FirebaseMessageMapper messageMapper;

    @Override
    public void pushAll(PushMessages pushMessages) {
        List<Message> firebaseMessages = messageMapper.mapToFirebaseMessages(pushMessages);
        try {
            FirebaseMessaging.getInstance().sendEach(firebaseMessages);
        } catch (FirebaseMessagingException e) {
            log.error(e.getMessage());
        }
    }
}

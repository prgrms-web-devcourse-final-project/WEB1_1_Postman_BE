package postman.bottler.notification.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import com.google.firebase.messaging.Message;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import postman.bottler.notification.domain.PushMessage;
import postman.bottler.notification.domain.PushMessages;

@SpringBootTest
class FirebaseMessageMapperTest {

    @Autowired
    private FirebaseMessageMapper firebaseMessageMapper;

    @DisplayName("도메인 메시지 객체를 파이어베이스 메시지 객체로 변환한다.")
    @Test
    void mapToFirebaseMessages() {
        // given
        PushMessage pushMessage1 = createPushMessage("token", "title", "content");
        PushMessage pushMessage2 = createPushMessage("token", "title", "content");
        PushMessages pushMessages = PushMessages.from(List.of(pushMessage1, pushMessage2));

        // when
        List<Message> firebaseMessages = firebaseMessageMapper.mapToFirebaseMessages(pushMessages);

        // then
        assertThat(firebaseMessages).hasSize(2)
                .extracting("token")
                .allMatch(token -> token.equals("token"));
        assertThat(firebaseMessages)
                .extracting("notification")
                .extracting("title", "body")
                .allMatch(tuple -> {
                    return tuple.equals(tuple("title", "content"));
                });
    }

    private PushMessage createPushMessage(String token, String title, String content) {
        return PushMessage.builder()
                .token(token)
                .title(title)
                .content(content)
                .build();
    }
}

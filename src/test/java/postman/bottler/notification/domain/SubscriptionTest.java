package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static postman.bottler.notification.domain.NotificationType.NEW_LETTER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("알림 구독 테스트")
public class SubscriptionTest {

    @DisplayName("구독 객체를 생성한다.")
    @Test
    void subscribe() {
        // given
        Long userId = 1L;
        String token = "token";

        // when
        Subscription subscription = Subscription.create(userId, token);

        // then
        assertThat(subscription.getUserId()).isEqualTo(1L);
        assertThat(subscription.getToken()).isEqualTo("token");
    }

    @DisplayName("알림을 보낼 메시지를 생성한다.")
    @Test
    void makeMessage() {
        // given
        Long userId = 1L;
        String token = "token";
        Subscription subscription = Subscription.create(userId, token);

        // when
        PushMessage pushMessage = subscription.makeMessage(NEW_LETTER);

        // then
        assertThat(pushMessage.getToken()).isEqualTo(token);
        assertThat(pushMessage.getTitle()).isEqualTo(NEW_LETTER.getTitle());
        assertThat(pushMessage.getContent()).isEqualTo(NEW_LETTER.getContent());
    }


}



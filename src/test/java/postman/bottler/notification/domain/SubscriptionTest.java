package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static postman.bottler.notification.domain.NotificationType.NEW_LETTER;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("알림 구독 테스트")
public class SubscriptionTest {

    @Test
    @DisplayName("구독 객체를 생성한다.")
    public void subscribe() {
        // GIVEN
        Long userId = 1L;
        String token = "token";

        // WHEN
        Subscription subscription = Subscription.create(userId, token);

        // THEN
        assertThat(subscription.getUserId()).isEqualTo(1L);
        assertThat(subscription.getToken()).isEqualTo("token");
    }

    @Test
    @DisplayName("푸시 알림 메시지 객체를 생성한다.")
    public void makeMessage() {
        // GIVEN
        long userId = 1L;
        String token = "token";
        Subscription subscription = Subscription.create(userId, token);

        // WHEN
        PushMessage pushMessage = subscription.makeMessage(NEW_LETTER);

        // THEN
        assertThat(pushMessage.getToken()).isEqualTo("token");
        assertThat(pushMessage.getTitle()).isEqualTo(NEW_LETTER.getTitle());
        assertThat(pushMessage.getContent()).isEqualTo(NEW_LETTER.getContent());
    }
}

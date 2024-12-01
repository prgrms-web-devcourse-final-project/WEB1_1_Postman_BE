package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("알림 구독 테스트")
public class SubscriptionTest {

    @Test
    @DisplayName("구독 객체를 생성한다.")
    public void subscribe() {
        // GIVEN

        // WHEN
        Subscription subscription = Subscription.create(1L, "token");

        // THEN
        assertThat(subscription.getUserId()).isEqualTo(1L);
        assertThat(subscription.getToken()).isEqualTo("token");
    }

    @Test
    @DisplayName("푸시 알림 메시지 객체를 생성한다.")
    public void makeMessage() {
        // GIVEN
        Subscription subscription = Subscription.create(1L, "token");

        // WHEN
        PushMessage pushMessage = subscription.makeMessage(NotificationType.NEW_LETTER);

        // THEN
        assertThat(pushMessage.getToken()).isEqualTo("token");
        assertThat(pushMessage.getTitle()).isEqualTo(NotificationType.NEW_LETTER.getTitle());
        assertThat(pushMessage.getContent()).isEqualTo(NotificationType.NEW_LETTER.getContent());
    }
}

package postman.bottler.notification.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static postman.bottler.notification.domain.NotificationType.NEW_LETTER;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SubscriptionsTest {

    @DisplayName("기기 구독 객체들을 생성한다.")
    @Test
    void from() {
        // given
        Subscription subscription1 = Subscription.create(1L, "token1");
        Subscription subscription2 = Subscription.create(1L, "token2");

        // when
        Subscriptions subscriptions = Subscriptions.from(List.of(subscription1, subscription2));

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(
                        tuple(1L, "token1"),
                        tuple(1L, "token2")
                );
    }

    @DisplayName("구독 정보들을 통해, 보낼 메시지 정보를 생성한다.")
    @Test
    void makeMessages() {
        // given
        Subscription subscription1 = Subscription.create(1L, "token1");
        Subscription subscription2 = Subscription.create(1L, "token2");
        Subscriptions subscriptions = Subscriptions.from(List.of(subscription1, subscription2));

        // when
        PushMessages pushMessages = subscriptions.makeMessages(NEW_LETTER);

        // then
        assertThat(pushMessages.getMessages()).hasSize(2)
                .extracting("token", "title", "content")
                .containsExactlyInAnyOrder(
                        tuple("token1", NEW_LETTER.getTitle(), NEW_LETTER.getContent()),
                        tuple("token2", NEW_LETTER.getTitle(), NEW_LETTER.getContent())
                );
    }

    @DisplayName("구독 정보가 null일 경우, 푸시 메시지 생성이 불가능하다.")
    @Test
    void isPushEnabledWithNull() {
        // given
        Subscriptions subscriptions = Subscriptions.from(null);

        // when
        Boolean result = subscriptions.isPushEnabled();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("구독 정보가 비어있을 경우, 푸시 메시지 생성이 불가능하다.")
    @Test
    void isPushEnabledWithEmpty() {
        // given
        Subscriptions subscriptions = Subscriptions.from(List.of());

        // when
        Boolean result = subscriptions.isPushEnabled();

        // then
        assertThat(result).isFalse();
    }
}

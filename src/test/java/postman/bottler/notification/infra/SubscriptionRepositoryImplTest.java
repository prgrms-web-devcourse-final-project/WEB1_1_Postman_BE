package postman.bottler.notification.infra;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.domain.Subscriptions;

@SpringBootTest
@Transactional
class SubscriptionRepositoryImplTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @DisplayName("사용자의 기기 구독 정보를 저장한다.")
    @Test
    void save() {
        // given
        Subscription subscription = Subscription.create(1L, "token");

        // when
        Subscription save = subscriptionRepository.save(subscription);

        // then
        assertThat(save.getUserId()).isEqualTo(1L);
        assertThat(save.getToken()).isEqualTo("token");
        assertThat(save.getId()).isNotNull();
    }

    @DisplayName("사용자의 기기 구독 정보를 조회한다.")
    @Test
    void findByUserId() {
        // given
        Long userId = 1L;
        Subscription subscription1 = Subscription.create(userId, "token1");
        Subscription subscription2 = Subscription.create(userId, "token2");
        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionRepository.findByUserId(1L);

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(
                        tuple(1L, "token1"),
                        tuple(1L, "token2")
                );
    }

    @DisplayName("사용자의 구독 정보가 없으면 빈 객체를 반환한다.")
    @Test
    void findByUserIdWithoutSubscription() {
        // given
        Long userId = 1L;

        // when
        Subscriptions subscriptions = subscriptionRepository.findByUserId(userId);

        // then
        assertThat(subscriptions.getSubscriptions()).isEmpty();
    }

    @DisplayName("모든 사용자의 구독 정보를 조회한다.")
    @Test
    void findAll() {
        // given
        Subscription subscription1 = Subscription.create(1L, "token1");
        Subscription subscription2 = Subscription.create(2L, "token2");
        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);

        // when
        Subscriptions subscriptions = subscriptionRepository.findAll();

        // then
        assertThat(subscriptions.getSubscriptions()).hasSize(2)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(
                        tuple(1L, "token1"),
                        tuple(2L, "token2")
                );
    }

    @DisplayName("사용자의 구독 정보를 삭제한다.")
    @Test
    void deleteAllByUserId() {
        // given
        Long userId = 1L;
        Subscription subscription = Subscription.create(userId, "token1");
        subscriptionRepository.save(subscription);

        // when
        subscriptionRepository.deleteAllByUserId(userId);

        // then
        Subscriptions subscriptions = subscriptionRepository.findByUserId(userId);
        assertThat(subscriptions.getSubscriptions()).isEmpty();
    }

    @DisplayName("특정 기기의 구독 정보를 삭제한다.")
    @Test
    void deleteByToken() {
        // given
        String token = "token1";
        Subscription subscription1 = Subscription.create(1L, token);
        Subscription subscription2 = Subscription.create(1L, "token2");
        subscriptionRepository.save(subscription1);
        subscriptionRepository.save(subscription2);

        // when
        subscriptionRepository.deleteByToken(token);

        // then
        Subscriptions subscriptions = subscriptionRepository.findByUserId(1L);
        assertThat(subscriptions.getSubscriptions()).hasSize(1)
                .extracting("userId", "token")
                .containsExactlyInAnyOrder(tuple(1L, "token2"));
    }

    @DisplayName("이미 구독된 유저의 기기라면, true를 반환한다.")
    @Test
    void isDuplicateWithDuplicateSubscription() {
        // given
        Subscription subscription = Subscription.create(1L, "token");
        Subscription duplicateSubscription = Subscription.create(1L, "token");
        subscriptionRepository.save(subscription);

        // when
        Boolean result = subscriptionRepository.isDuplicate(duplicateSubscription);

        // then
        assertThat(result).isTrue();
    }

    @DisplayName("저장되지 않은 구독 정보라면, false를 반환한다.")
    @Test
    void isDuplicate() {
        // given
        Subscription subscription = Subscription.create(1L, "token1");
        Subscription notDuplicateSubscription = Subscription.create(1L, "token2");
        subscriptionRepository.save(subscription);

        // when
        Boolean result = subscriptionRepository.isDuplicate(notDuplicateSubscription);

        // then
        assertThat(result).isFalse();
    }
}

package postman.bottler.notification.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.application.service.SubscriptionService;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.application.dto.response.SubscriptionResponseDTO;

@DisplayName("알림 구독 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @InjectMocks
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Test
    @DisplayName("알림 구독을 허용한다.")
    public void subscribe() {
        // GIVEN
        when(subscriptionRepository.save(any())).thenReturn(Subscription.create(1L, "token"));

        // WHEN
        SubscriptionResponseDTO response = subscriptionService.subscribe(1L, "token");

        // THEN
        assertThat(response.userId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("해당 유저의 알림을 비허용한다.")
    public void unsubscribeAll() {
        // GIVEN
        Long userId = 1L;

        // WHEN
        subscriptionService.unsubscribeAll(userId);

        // THEN
        verify(subscriptionRepository, times(1)).deleteAllByUserId(userId);
    }

    @Test
    @DisplayName("특정 기기의 알림을 비허용한다.")
    public void unsubscribe() {
        // GIVEN
        String token = "token";

        // WHEN
        subscriptionService.unsubscribe(token);

        // THEN
        verify(subscriptionRepository, times(1)).deleteByToken(token);
    }
}

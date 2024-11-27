package postman.bottler.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.dto.response.SubscriptionResponseDTO;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionResponseDTO subscribe(Long userId, String token) {
        Subscription subscribe = Subscription.create(userId, token);
        Subscription save = subscriptionRepository.save(subscribe);
        return SubscriptionResponseDTO.from(save);
    }

    @Transactional
    public void unsubscribeAll(Long userId) {
        subscriptionRepository.deleteAllByUserId(userId);
    }
}

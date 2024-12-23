package postman.bottler.notification.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.dto.response.SubscriptionResponseDTO;
import postman.bottler.notification.exception.DuplicateTokenException;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;

    @Transactional
    public SubscriptionResponseDTO subscribe(Long userId, String token) {
        Subscription subscribe = Subscription.create(userId, token);
        if (subscriptionRepository.isDuplicate(subscribe)) {
            throw new DuplicateTokenException();
        }
        Subscription save = subscriptionRepository.save(subscribe);
        return SubscriptionResponseDTO.from(save);
    }

    @Transactional
    public void unsubscribeAll(Long userId) {
        subscriptionRepository.deleteAllByUserId(userId);
    }

    @Transactional
    public void unsubscribe(String token) {
        subscriptionRepository.deleteByToken(token);
    }
}

package postman.bottler.notification.service;

import java.util.List;
import postman.bottler.notification.domain.Subscription;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);

    List<Subscription> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}

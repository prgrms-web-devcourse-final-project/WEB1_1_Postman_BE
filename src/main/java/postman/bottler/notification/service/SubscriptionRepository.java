package postman.bottler.notification.service;

import postman.bottler.notification.domain.Subscription;

public interface SubscriptionRepository {
    Subscription save(Subscription subscription);
}

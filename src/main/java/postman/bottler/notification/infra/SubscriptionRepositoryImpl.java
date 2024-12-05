package postman.bottler.notification.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.service.SubscriptionRepository;

@Repository
@RequiredArgsConstructor
public class SubscriptionRepositoryImpl implements SubscriptionRepository {
    private final JpaSubscriptionRepository repository;

    @Override
    public Subscription save(Subscription subscription) {
        SubscriptionEntity save = repository.save(SubscriptionEntity.from(subscription));
        return save.toDomain();
    }

    @Override
    public Subscriptions findByUserId(Long userId) {
        List<Subscription> subscriptions = repository.findByUserId(userId).stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
        return Subscriptions.from(subscriptions);
    }

    @Override
    public Subscriptions findAll() {
        List<Subscription> subscriptions = repository.findAll().stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
        return Subscriptions.from(subscriptions);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        repository.deleteAllByUserId(userId);
    }

    @Override
    public void deleteByToken(String token) {
        repository.deleteByToken(token);
    }
}

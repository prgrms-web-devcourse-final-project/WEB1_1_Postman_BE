package postman.bottler.notification.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.notification.domain.Subscription;
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
}

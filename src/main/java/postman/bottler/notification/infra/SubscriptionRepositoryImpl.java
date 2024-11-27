package postman.bottler.notification.infra;

import java.util.List;
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

    @Override
    public List<Subscription> findByUserId(Long userId) {
        List<SubscriptionEntity> finds = repository.findByUserId(userId);
        return finds.stream()
                .map(SubscriptionEntity::toDomain)
                .toList();
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        repository.deleteAllByUserId(userId);
    }
}

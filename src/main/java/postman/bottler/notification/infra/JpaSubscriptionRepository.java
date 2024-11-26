package postman.bottler.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
}

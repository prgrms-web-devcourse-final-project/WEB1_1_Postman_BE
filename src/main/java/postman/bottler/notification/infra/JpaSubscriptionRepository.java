package postman.bottler.notification.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaSubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    void deleteByToken(String token);
}

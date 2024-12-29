package postman.bottler.notification.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.notification.infra.entity.SubscriptionEntity;

public interface SubscriptionJpaRepository extends JpaRepository<SubscriptionEntity, Long> {
    List<SubscriptionEntity> findByUserId(Long userId);

    void deleteAllByUserId(Long userId);

    void deleteByToken(String token);

    Boolean existsByUserIdAndToken(Long userId, String token);
}

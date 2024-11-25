package postman.bottler.notification.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

    List<NotificationEntity> findByReceiverOrderByCreateAtDesc(Long receiver);
}

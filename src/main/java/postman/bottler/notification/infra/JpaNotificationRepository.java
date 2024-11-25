package postman.bottler.notification.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNotificationRepository extends JpaRepository<NotificationEntity, Long> {

}

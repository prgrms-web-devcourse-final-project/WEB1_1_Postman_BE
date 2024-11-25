package postman.bottler.notification.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.service.NotificationRepository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final JpaNotificationRepository repository;

    @Override
    public Notification save(Notification notification) {
        return repository.save(NotificationEntity.from(notification)).toDomain();
    }
}

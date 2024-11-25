package postman.bottler.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.notification.domain.Notification;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public Notification sendNotification(String type, Long userId, Long letterId) {
        Notification notification = Notification.of(type, userId, letterId);
        // TODO 푸시 알림 보내는 로직 추가
        return notificationRepository.save(notification);
    }
}

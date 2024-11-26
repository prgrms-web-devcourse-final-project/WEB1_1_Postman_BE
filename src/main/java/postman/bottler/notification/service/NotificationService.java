package postman.bottler.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.Subscription;
import postman.bottler.notification.dto.response.NotificationResponseDTO;
import postman.bottler.notification.dto.response.SubscriptionResponseDTO;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionResponseDTO subscribe(Long userId, String token) {
        Subscription subscribe = Subscription.create(userId, token);
        Subscription save = subscriptionRepository.save(subscribe);
        return SubscriptionResponseDTO.from(save);
    }

    @Transactional
    public NotificationResponseDTO sendNotification(String type, Long userId, Long letterId) {
        Notification notification = Notification.create(type, userId, letterId);
        // TODO 푸시 알림 보내는 로직 추가
        return NotificationResponseDTO.from(notificationRepository.save(notification));
    }

    @Transactional
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiver(userId);
        List<NotificationResponseDTO> result = notifications.stream()
                .map(NotificationResponseDTO::from)
                .toList();
        notifications.stream()
                .filter(notification -> !notification.getIsRead())
                .forEach(Notification::read);
        notificationRepository.updateNotifications(notifications);
        return result;
    }
}

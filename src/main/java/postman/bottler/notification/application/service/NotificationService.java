package postman.bottler.notification.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.application.PushNotificationProvider;
import postman.bottler.notification.application.repository.NotificationRepository;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.application.dto.response.NotificationResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PushNotificationProvider pushNotificationProvider;

    @Transactional
    public NotificationResponseDTO sendNotification(NotificationType type, Long userId, Long letterId, String label) {
        Notification notification = Notification.create(type, userId, letterId, label);
        Subscriptions subscriptions = subscriptionRepository.findByUserId(userId);
        NotificationResponseDTO result = NotificationResponseDTO.from(notificationRepository.save(notification));
        if (subscriptions.isPushEnabled()) {
            PushMessages pushMessages = subscriptions.makeMessages(type);
            pushNotificationProvider.pushAll(pushMessages);
        }
        return result;
    }

    @Transactional
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        Notifications notifications = notificationRepository.findByReceiver(userId);
        notifications.orderByCreatedAt();
        List<NotificationResponseDTO> result = notifications.createDTO();
        notificationRepository.updateNotifications(notifications.markAsRead());
        return result;
    }

    @Transactional
    public void sendKeywordNotifications(List<RecommendNotificationRequestDTO> requests) {
        requests.forEach(request -> {
            notificationRepository.save(Notification.create(NotificationType.NEW_LETTER, request.userId(),
                    request.letterId(), request.label()));
        });
        Subscriptions allSubscriptions = subscriptionRepository.findAll();
        if (allSubscriptions.isPushEnabled()) {
            PushMessages pushMessages = allSubscriptions.makeMessages(NotificationType.NEW_LETTER);
            pushNotificationProvider.pushAll(pushMessages);
        }
    }
}
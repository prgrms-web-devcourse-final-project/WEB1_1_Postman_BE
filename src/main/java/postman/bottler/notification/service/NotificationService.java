package postman.bottler.notification.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

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
            PushMessages pushMessages = subscriptions.makeMessages(notification);
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
        PushMessages pushMessages = PushMessages.from(new ArrayList<>());
        Subscriptions allSubscriptions = subscriptionRepository.findAll();
        requests.forEach(request -> {
            Notification notification = Notification.create(NotificationType.NEW_LETTER, request.userId(),
                    request.letterId(), request.label());
            Subscriptions userSubscriptions = allSubscriptions.getSubscriptionsByUserId(request.userId());
            pushMessages.add(userSubscriptions.makeMessages(notification));
            notificationRepository.save(notification);
        });
        if (allSubscriptions.isPushEnabled()) {
            pushNotificationProvider.pushAll(pushMessages);
        }
    }
}

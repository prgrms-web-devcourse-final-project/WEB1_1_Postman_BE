package postman.bottler.notification.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.notification.application.PushNotificationProvider;
import postman.bottler.notification.application.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.application.dto.response.NotificationResponseDTO;
import postman.bottler.notification.application.dto.response.UnreadNotificationResponseDTO;
import postman.bottler.notification.application.repository.NotificationRepository;
import postman.bottler.notification.application.repository.SubscriptionRepository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PushNotificationProvider pushNotificationProvider;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendBanNotification(Long userId) {
        try {
            sendNotification(NotificationType.BAN, userId, null, null);
        } catch (RuntimeException e) {
            log.error("알림 저장 실패 : {}", e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendWarningNotification(Long userId) {
        try {
            sendNotification(NotificationType.WARNING, userId, null, null);
        } catch (RuntimeException e) {
            log.error("알림 저장 실패 : {}", e.getMessage());
        }
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void sendLetterNotification(NotificationType type, Long userId, Long letterId, String label) {
        try {
            sendNotification(type, userId, letterId, label);
        } catch (RuntimeException e) {
            log.error("알림 저장 실패 : {}", e.getMessage());
        }
    }

    public NotificationResponseDTO sendNotification(NotificationType type, Long userId, Long letterId, String label) {
        Notification notification = Notification.create(type, userId, letterId, label);
        Subscriptions subscriptions = subscriptionRepository.findByUserId(userId);
        NotificationResponseDTO result = NotificationResponseDTO.from(notificationRepository.save(notification));
        if (subscriptions.isPushEnabled()) {
            pushMessage(type, subscriptions);
        }
        return result;
    }

    private void pushMessage(NotificationType type, Subscriptions subscriptions) {
        PushMessages pushMessages = subscriptions.makeMessages(type);
        pushNotificationProvider.pushAll(pushMessages);
    }

    @Transactional(readOnly = true)
    public UnreadNotificationResponseDTO getUnreadNotificationCount(Long userId) {
        Notifications notifications = notificationRepository.findByReceiver(userId);
        return new UnreadNotificationResponseDTO(notifications.getUnreadCount());
    }

    @Transactional
    public List<NotificationResponseDTO> getUserNotifications(Long userId) {
        Notifications notifications = notificationRepository.findByReceiver(userId);
        notifications.orderByCreatedAt();
        List<NotificationResponseDTO> result = NotificationResponseDTO.from(notifications);
        notificationRepository.updateNotifications(notifications.markAsRead());
        return result;
    }

    public void sendKeywordNotifications(List<RecommendNotificationRequestDTO> requests) {
        requests.forEach(request -> {
            notificationRepository.save(Notification.create(NotificationType.NEW_LETTER, request.userId(),
                    request.letterId(), request.label()));
        });
        Subscriptions allSubscriptions = subscriptionRepository.findAll();
        if (allSubscriptions.isPushEnabled()) {
            pushMessage(NotificationType.NEW_LETTER, allSubscriptions);
        }
    }
}

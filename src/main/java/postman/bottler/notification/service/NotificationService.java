package postman.bottler.notification.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.service.LetterRepository;
import postman.bottler.mapletter.service.MapLetterRepository;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.domain.Notifications;
import postman.bottler.notification.domain.PushMessages;
import postman.bottler.notification.domain.Subscriptions;
import postman.bottler.notification.dto.request.NotificationLabelRequestDTO;
import postman.bottler.notification.dto.request.RecommendNotificationRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final NotificationRepository notificationRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PushNotificationProvider pushNotificationProvider;

    private final MapLetterRepository mapLetterRepository;
    private final LetterRepository keywordLetterRepository;

    @Transactional
    public NotificationResponseDTO sendNotification(NotificationType type, Long userId, Long letterId) {
        Notification notification = Notification.create(type, userId, letterId);
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
        List<Long> mapIds = notifications.extractMapIds();
        List<NotificationLabelRequestDTO> mapLabels = mapLetterRepository.findAllByIds(mapIds).stream()
                .map(letter -> new NotificationLabelRequestDTO(letter.getId(), letter.getLabel())).toList();
        notifications.setMapLabels(mapLabels);
        List<Long> keywordIds = notifications.extractKeywordIds();
        List<NotificationLabelRequestDTO> keywordLabels = keywordLetterRepository.findAllByIds(keywordIds)
                .stream().map(letter -> new NotificationLabelRequestDTO(letter.getId(), letter.getLabel())).toList();
        notifications.setKeywordLabels(keywordLabels);
        notifications.orderByCreatedAt();
        List<NotificationResponseDTO> result = notifications.createDTO();
        notificationRepository.updateNotifications(notifications.markAsRead());
        return result;
    }

    @Transactional
    public void sendKeywordNotifications(List<RecommendNotificationRequestDTO> requests) {
        requests.forEach(request -> {
            notificationRepository.save(Notification.create(NotificationType.NEW_LETTER, request.userId(),
                    request.letterId()));
        });
        Subscriptions allSubscriptions = subscriptionRepository.findAll();
        if (allSubscriptions.isPushEnabled()) {
            PushMessages pushMessages = allSubscriptions.makeMessages(NotificationType.NEW_LETTER);
            pushNotificationProvider.pushAll(pushMessages);
        }
    }
}

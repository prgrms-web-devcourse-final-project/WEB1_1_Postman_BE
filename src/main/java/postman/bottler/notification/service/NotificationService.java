package postman.bottler.notification.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationResponseDTO sendNotification(String type, Long userId, Long letterId) {
        Notification notification = Notification.of(type, userId, letterId);
        // TODO 푸시 알림 보내는 로직 추가
        return NotificationResponseDTO.from(notificationRepository.save(notification));
    }
}

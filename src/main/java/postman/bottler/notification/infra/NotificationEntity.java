package postman.bottler.notification.infra;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;

@Entity
@Table(name = "notification")
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long receiver;

    private NotificationType type;

    private Long letterId;

    private LocalDateTime createAt;

    private Boolean isRead;

    public static NotificationEntity from(Notification notification) {
        return NotificationEntity.builder()
                .id(notification.getId())
                .receiver(notification.getReceiver())
                .type(notification.getType())
                .letterId(notification instanceof LetterNotification ?
                        ((LetterNotification) notification).getLetterId() : null)
                .createAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .build();
    }

    public Notification toDomain() {
        return Notification.of(id, type, receiver, letterId, isRead);
    }
}

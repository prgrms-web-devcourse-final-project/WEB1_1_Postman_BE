package postman.bottler.notification.domain;

import lombok.Getter;
import postman.bottler.notification.exception.InvalidNotificationRequestException;

@Getter
public class LetterNotification extends Notification {

    private long letterId;

    protected LetterNotification(NotificationType type, long receiver, Long letterId) {
        super(type, receiver);
        validateLetterId(letterId);
        this.letterId = letterId;
    }

    private void validateLetterId(Long letterId) {
        if (letterId == null) {
            throw new InvalidNotificationRequestException("편지 관련 알림은 편지 ID가 있어야 합니다.");
        }
    }
}

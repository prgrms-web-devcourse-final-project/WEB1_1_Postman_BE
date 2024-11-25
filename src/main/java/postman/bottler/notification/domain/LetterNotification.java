package postman.bottler.notification.domain;

import lombok.Getter;
import postman.bottler.notification.exception.InvalidNotificationRequestException;
import postman.bottler.notification.exception.NoLetterIdException;

@Getter
public class LetterNotification extends Notification {
    private long letterId;

    protected LetterNotification(NotificationType type, long receiver, Long letterId, Boolean isRead) {
        super(type, receiver, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
    }

    protected LetterNotification(Long id, NotificationType type, long receiver, Long letterId, Boolean isRead) {
        super(id, type, receiver, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
    }

    private void validateLetterId(Long letterId) {
        if (letterId == null) {
            throw new NoLetterIdException();
        }
    }
}

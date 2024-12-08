package postman.bottler.notification.domain;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import postman.bottler.notification.exception.NoLetterIdException;

@Getter
public class LetterNotification extends Notification {
    private final long letterId;

    @Setter
    private String label;

    protected LetterNotification(NotificationType type, long receiver, Long letterId, Boolean isRead) {
        super(type, receiver, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
    }

    protected LetterNotification(UUID id, NotificationType type, long receiver,
                                 Long letterId, LocalDateTime createdAt, Boolean isRead) {
        super(id, type, receiver, createdAt, isRead);
        validateLetterId(letterId);
        this.letterId = letterId;
    }

    private void validateLetterId(Long letterId) {
        if (letterId == null) {
            throw new NoLetterIdException();
        }
    }
}

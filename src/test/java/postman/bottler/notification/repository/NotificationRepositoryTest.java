package postman.bottler.notification.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.infra.NotificationRepositoryImpl;
import postman.bottler.notification.service.NotificationRepository;

@DataJpaTest
@Import(NotificationRepositoryImpl.class)
public class NotificationRepositoryTest {
    @Autowired
    private NotificationRepository notificationRepository;

    @Test
    @DisplayName("알림 저장")
    public void save() {
        // GIVEN
        Notification notification = Notification.of("NEW_LETTER", 1L, 1L);

        // WHEN
        Notification save = notificationRepository.save(notification);

        // THEN
        assertThat(save.getType()).isEqualTo(NotificationType.NEW_LETTER);
        assertThat(save.getReceiver()).isEqualTo(notification.getReceiver());
        assertThat(save).isInstanceOf(LetterNotification.class);
        assertThat(((LetterNotification) save).getLetterId()).isEqualTo(1L);
    }
}

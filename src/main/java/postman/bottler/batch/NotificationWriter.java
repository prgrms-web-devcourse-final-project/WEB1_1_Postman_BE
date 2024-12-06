package postman.bottler.batch;

import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.service.LetterBoxRepository;
import postman.bottler.notification.domain.LetterNotification;
import postman.bottler.notification.domain.Notification;
import postman.bottler.notification.service.NotificationRepository;

@RequiredArgsConstructor
public class NotificationWriter implements ItemWriter<Notification> {
    private final NotificationRepository repository;
    private final LetterBoxRepository letterBoxRepository;

    @Override
    public void write(Chunk<? extends Notification> chunk) throws Exception {
        for (Notification notification : chunk) {
            letterBoxRepository.save(LetterBox.builder()
                    .userId(notification.getReceiver())
                    .letterId(((LetterNotification) notification).getLetterId())
                    .letterType(LetterType.LETTER)
                    .boxType(BoxType.RECEIVE)
                    .createdAt(LocalDateTime.now())
                    .build());
            repository.save(notification);
        }
    }
}

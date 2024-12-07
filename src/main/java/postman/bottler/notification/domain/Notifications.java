package postman.bottler.notification.domain;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import postman.bottler.notification.dto.request.NotificationLabelRequestDTO;
import postman.bottler.notification.dto.response.NotificationResponseDTO;

@Builder
@Getter
public class Notifications {
    private List<Notification> notifications;

    public static Notifications from(List<Notification> notifications) {
        return Notifications.builder()
                .notifications(notifications)
                .build();
    }

    public void orderByCreatedAt() {
        notifications.sort(Comparator.comparing(Notification::getCreatedAt).reversed());
    }

    public Notifications markAsRead() {
        List<Notification> changed = new ArrayList<>();
        for (Notification notification : notifications) {
            if (!notification.getIsRead()) {
                changed.add(notification.read());
            }
        }
        return new Notifications(changed);
    }

    public List<Long> extractMapIds() {
        List<Long> ids = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.isMapLetterNotification()) {
                ids.add(((LetterNotification) notification).getLetterId());
            }
        }
        return ids;
    }

    public List<Long> extractKeywordIds() {
        List<Long> ids = new ArrayList<>();
        for (Notification notification : notifications) {
            if (notification.isKeywordLetterNotification()) {
                ids.add(((LetterNotification) notification).getLetterId());
            }
        }
        return ids;
    }

    public void setMapLabels(List<NotificationLabelRequestDTO> mapLabels) {
        for (Notification notification : notifications) {
            if (!notification.isMapLetterNotification()) {
                continue;
            }
            mapLabels.stream()
                    .filter(label -> label.letterId() == ((LetterNotification) notification).getLetterId())
                    .forEach(label -> ((LetterNotification) notification).setLabel(label.label()));
        }
    }

    public void setKeywordLabels(List<NotificationLabelRequestDTO> keywordLabels) {
        for (Notification notification : notifications) {
            if (!notification.isKeywordLetterNotification()) {
                continue;
            }
            keywordLabels.stream()
                    .filter(label -> label.letterId() == ((LetterNotification) notification).getLetterId())
                    .forEach(label -> ((LetterNotification) notification).setLabel(label.label()));
        }
    }

    public List<Long> getLetterIds() {
        return notifications.stream()
                .filter(Notification::isLetterNotification)
                .map(notification -> ((LetterNotification) notification).getLetterId())
                .toList();
    }

    public List<NotificationResponseDTO> createDTO() {
        return notifications.stream()
                .map(NotificationResponseDTO::from)
                .toList();
    }
}

package postman.bottler.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Subscription {
    private Long id;

    private Long userId;

    private String token;

    public static Subscription create(Long userId, String token) {
        return Subscription.builder()
                .userId(userId)
                .token(token)
                .build();
    }

    public PushMessage makeMessage(Notification notification) {
        return PushMessage.builder()
                .token(token)
                .title(notification.getType().getTitle())
                .content(notification.getType().getContent())
                .image(notification.getImage())
                .build();
    }
}

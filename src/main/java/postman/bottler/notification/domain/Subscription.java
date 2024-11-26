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

    public PushMessage makeMessage(NotificationType type) {
        return PushMessage.builder()
                .token(token)
                .title(type.getTitle())
                .content(type.getContent())
                .build();
    }
}

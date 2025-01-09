package postman.bottler.notification.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Subscriptions {
    private List<Subscription> subscriptions;

    public static Subscriptions from(List<Subscription> subscriptions) {
        return Subscriptions.builder()
                .subscriptions(subscriptions)
                .build();
    }

    public PushMessages makeMessages(NotificationType type) {
        List<PushMessage> messages = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            messages.add(subscription.makeMessage(type));
        }
        return PushMessages.from(messages);
    }

    public Boolean isPushEnabled() {
        return subscriptions != null && !subscriptions.isEmpty();
    }
}

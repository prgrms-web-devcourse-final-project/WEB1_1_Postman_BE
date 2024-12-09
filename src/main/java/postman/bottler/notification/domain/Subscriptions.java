package postman.bottler.notification.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

@Builder
public class Subscriptions {
    private List<Subscription> subscriptions;

    public static Subscriptions from(List<Subscription> subscriptions) {
        return Subscriptions.builder()
                .subscriptions(subscriptions)
                .build();
    }

    public PushMessages makeMessages(Notification notification) {
        List<PushMessage> messages = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            messages.add(subscription.makeMessage(notification));
        }
        return PushMessages.from(messages);
    }

    public Boolean isPushEnabled() {
        return subscriptions != null && !subscriptions.isEmpty();
    }

    public Subscriptions getSubscriptionsByUserId(Long userId) {
        List<Subscription> userSubscriptions = new ArrayList<>();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId().equals(userId)) {
                userSubscriptions.add(subscription);
            }
        }
        return Subscriptions.from(userSubscriptions);
    }
}

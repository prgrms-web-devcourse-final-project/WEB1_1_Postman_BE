package postman.bottler.notification.application.dto.response;

import postman.bottler.notification.domain.Subscription;

public record SubscriptionResponseDTO(
        long userId
) {
    public static SubscriptionResponseDTO from(Subscription subscription) {
        return new SubscriptionResponseDTO(subscription.getUserId());
    }
}

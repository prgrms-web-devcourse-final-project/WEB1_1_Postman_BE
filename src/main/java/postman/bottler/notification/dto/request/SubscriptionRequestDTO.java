package postman.bottler.notification.dto.request;

public record SubscriptionRequestDTO(
        String deviceToken,
        Long userId
) {
}

package postman.bottler.notification.dto.request;

public record SubscriptionRequestDTO(
        String token,
        Long userId
) {
}

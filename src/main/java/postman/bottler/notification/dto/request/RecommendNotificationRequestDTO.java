package postman.bottler.notification.dto.request;

public record RecommendNotificationRequestDTO(
        Long userId,
        Long letterId
) {
    public static RecommendNotificationRequestDTO of(Long userId, Long letterId) {
        return new RecommendNotificationRequestDTO(userId, letterId);
    }
}

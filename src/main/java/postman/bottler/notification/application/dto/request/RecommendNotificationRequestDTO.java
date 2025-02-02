package postman.bottler.notification.application.dto.request;

public record RecommendNotificationRequestDTO(
        Long userId,
        Long letterId,
        String label
) {
    public static RecommendNotificationRequestDTO of(Long userId, Long letterId, String label) {
        return new RecommendNotificationRequestDTO(userId, letterId, label);
    }
}

package postman.bottler.notification.application.dto.request;

public record NotificationLabelRequestDTO(
        Long letterId,
        String label
) {
}

package postman.bottler.user.dto.request;

public record AuthEmailRequestDTO(
        String email,
        String code
) {
}

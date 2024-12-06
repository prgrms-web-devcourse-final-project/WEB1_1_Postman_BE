package postman.bottler.user.dto.response;

public record SignInDTO(
        String accessToken,
        String refreshToken
) {
}

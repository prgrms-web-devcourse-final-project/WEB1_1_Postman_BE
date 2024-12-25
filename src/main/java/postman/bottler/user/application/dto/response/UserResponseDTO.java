package postman.bottler.user.application.dto.response;

import postman.bottler.user.domain.Provider;
import postman.bottler.user.domain.User;

public record UserResponseDTO(
        String email,
        String nickname,
        String profileImageUrl,
        Provider provider,
        int warningCount
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getEmail(), user.getNickname(), user.getImageUrl(), user.getProvider(),
                user.getWarningCount());
    }
}

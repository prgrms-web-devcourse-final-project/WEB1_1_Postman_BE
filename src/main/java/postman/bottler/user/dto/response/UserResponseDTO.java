package postman.bottler.user.dto.response;

import postman.bottler.user.domain.Provider;
import postman.bottler.user.domain.User;

public record UserResponseDTO(
        Long userId,
        String nickname,
        String profileImageUrl,
        Provider provider
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getUserId(), user.getNickname(), user.getImageUrl(), user.getProvider());
    }
}

package postman.bottler.user.dto.response;

import postman.bottler.user.domain.User;

public record UserResponseDTO(
        String nickname,
        String profileImageUrl
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getNickname(), user.getImageUrl());
    }
}

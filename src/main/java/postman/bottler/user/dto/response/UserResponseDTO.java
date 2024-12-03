package postman.bottler.user.dto.response;

import postman.bottler.user.domain.User;

public record UserResponseDTO(
        Long userId,
        String nickname,
        String profileImageUrl
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(user.getUserId(), user.getNickname(), user.getImageUrl());
    }
}

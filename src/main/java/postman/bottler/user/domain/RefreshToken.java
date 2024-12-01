package postman.bottler.user.domain;

import lombok.Getter;

@Getter
public class RefreshToken {
    private final String email;
    private final String refreshToken;

    private RefreshToken(String email, String refreshToken) {
        this.email = email;
        this.refreshToken = refreshToken;
    }

    public static RefreshToken createRefreshToken(String email, String refreshToken) {
        return new RefreshToken(email, refreshToken);
    }
}

package postman.bottler.user.application.repository;

import postman.bottler.user.domain.RefreshToken;

public interface RefreshTokenRepository {
    void createRefreshToken(RefreshToken refreshToken);

    String findEmailByRefreshToken(String refreshToken);

    void deleteByEmail(String email);
}

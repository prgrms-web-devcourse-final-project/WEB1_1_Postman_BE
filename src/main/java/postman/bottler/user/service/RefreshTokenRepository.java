package postman.bottler.user.service;

import postman.bottler.user.domain.RefreshToken;

public interface RefreshTokenRepository {
    void createRefreshToken(RefreshToken refreshToken);
    String findEmailByRefreshToken(String refreshToken);
    void deleteByEmail(String email);
}

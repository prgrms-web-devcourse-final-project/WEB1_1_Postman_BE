package postman.bottler.user.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.RefreshToken;
import postman.bottler.user.exception.TokenException;
import postman.bottler.user.infra.entity.RefreshTokenEntity;
import postman.bottler.user.service.RefreshTokenRepository;

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;

    @Override
    public void createRefreshToken(RefreshToken refreshToken) {
        Optional<RefreshTokenEntity> refreshTokenEntityOpt = refreshTokenJpaRepository.findByEmail(refreshToken.getEmail());

        if (refreshTokenEntityOpt.isEmpty()) {
            refreshTokenJpaRepository.save(RefreshTokenEntity.from(refreshToken));
        } else {
            RefreshTokenEntity refreshTokenEntity = refreshTokenEntityOpt.get();
            refreshTokenEntity.updateRefreshToken(refreshToken.getRefreshToken());
            refreshTokenJpaRepository.save(refreshTokenEntity);
        }
    }

    @Override
    public String findEmailByRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenJpaRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new TokenException("유효하지 않은 jwt 토큰입니다."));
        return refreshTokenEntity.getEmail();
    }
}

package postman.bottler.user.infra;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.user.infra.entity.RefreshTokenEntity;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByEmail(String email);
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    void deleteByEmail(String email);
}

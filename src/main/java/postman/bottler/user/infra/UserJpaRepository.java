package postman.bottler.user.infra;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>  {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
}

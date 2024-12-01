package postman.bottler.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>  {
}

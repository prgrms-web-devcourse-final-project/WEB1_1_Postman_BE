package postman.bottler.user.infra;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<UserEntity> findByEmail(String email);

    @Modifying
    @Query("update UserEntity u set u.isBanned = false, u.role = postman.bottler.user.domain.Role.USER "
            + "where u.userId in :ids")
    void unbanUsers(List<Long> ids);
}

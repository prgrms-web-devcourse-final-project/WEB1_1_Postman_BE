package postman.bottler.user.infra;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long>  {
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.email = :email AND u.isDeleted = false")
    boolean existsByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END " +
            "FROM UserEntity u WHERE u.nickname = :nickname AND u.isDeleted = false")
    boolean existsByNickname(String nickname);

    Optional<UserEntity> findByEmail(String email);
}

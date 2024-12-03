package postman.bottler.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.user.infra.entity.ProfileImageEntity;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImageEntity, Long> {
    boolean existsByImageUrl(String newProfileImage);

    @Query("SELECT p FROM ProfileImageEntity p ORDER BY FUNCTION('RAND') LIMIT 1")
    String findRandomProfileImage();
}

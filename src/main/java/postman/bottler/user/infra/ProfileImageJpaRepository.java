package postman.bottler.user.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.user.infra.entity.ProfileImageEntity;

public interface ProfileImageJpaRepository extends JpaRepository<ProfileImageEntity, Long> {
    boolean existsByImageUrl(String newProfileImage);
}

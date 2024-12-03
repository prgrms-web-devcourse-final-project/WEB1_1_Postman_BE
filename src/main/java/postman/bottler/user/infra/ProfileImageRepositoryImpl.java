package postman.bottler.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.ProfileImage;
import postman.bottler.user.infra.entity.ProfileImageEntity;
import postman.bottler.user.service.ProfileImageRepository;

@Repository
@RequiredArgsConstructor
public class ProfileImageRepositoryImpl implements ProfileImageRepository {
    private final ProfileImageJpaRepository profileImageJpaRepository;

    @Override
    public void save(ProfileImage profileImage) {
        profileImageJpaRepository.save(ProfileImageEntity.from(profileImage));
    }

    @Override
    public boolean existsByUrl(String newProfileImage) {
        return profileImageJpaRepository.existsByImageUrl(newProfileImage);
    }
}

package postman.bottler.user.service;

import postman.bottler.user.domain.ProfileImage;

public interface ProfileImageRepository {
    void save(ProfileImage profileImage);
    boolean existsByUrl(String newProfileImage);
}

package postman.bottler.user.applications.repository;

import postman.bottler.user.domain.ProfileImage;

public interface ProfileImageRepository {
    void save(ProfileImage profileImage);
    boolean existsByUrl(String newProfileImage);
    String findProfileImage();
}

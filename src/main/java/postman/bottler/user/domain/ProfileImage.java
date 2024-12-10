package postman.bottler.user.domain;

import lombok.Getter;

@Getter
public class ProfileImage {
    private String imageUrl;

    private ProfileImage(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static ProfileImage createProfileImg(String profileImageUrl) {
        return new ProfileImage(profileImageUrl);
    }
}

package postman.bottler.user.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.user.domain.ProfileImage;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "profile_image")
public class ProfileImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileImageId;

    private String imageUrl;

    public static ProfileImageEntity from(ProfileImage profileImage) {
        return ProfileImageEntity.builder()
                .imageUrl(profileImage.getImageUrl())
                .build();
    }
}

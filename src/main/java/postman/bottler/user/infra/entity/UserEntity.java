package postman.bottler.user.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.UserException;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBanned;

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDeleted(user.isDeleted())
                .isBanned(user.isBanned())
                .build();
    }

    public static User toUser(UserEntity userEntity) {
        if (userEntity.isDeleted) throw new UserException("탈퇴한 유저입니다.");
        return userEntity.to();
    }

    public User to() {
        return User.createUser(this.userId, this.email, this.password, this.nickname, this.imageUrl, this.createdAt, this.updatedAt, this.isDeleted, this.isBanned);
    }

    public void updateIsDelete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
}

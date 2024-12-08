package postman.bottler.user.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.user.domain.Provider;
import postman.bottler.user.domain.Role;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.UserException;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String email;

    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private boolean isDeleted;

    @Column(nullable = false)
    private int warningCount;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    public static UserEntity from(User user) {
        return UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .nickname(user.getNickname())
                .imageUrl(user.getImageUrl())
                .role(user.getRole())
                .provider(user.getProvider())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .isDeleted(user.isDeleted())
                .warningCount(user.getWarningCount())
                .build();
    }

    public static User toUser(UserEntity userEntity) {
        if (userEntity.isDeleted) {
            throw new UserException("탈퇴한 유저입니다.");
        }
        return userEntity.to();
    }

    public User to() {
        return User.createUser(this.userId, this.email, this.password, this.nickname, this.imageUrl, this.role,
                this.provider, this.createdAt, this.updatedAt, this.isDeleted, this.warningCount);
    }

    public void updateIsDelete() {
        this.isDeleted = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void updateBanUser(User user) {
        this.updatedAt = user.getUpdatedAt();
        this.warningCount = user.getWarningCount();
        this.role = user.getRole();
    }
}

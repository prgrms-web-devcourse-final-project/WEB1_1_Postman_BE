package postman.bottler.user.domain;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class User {
    private Long userId;
    private final String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private Role role;
    private Provider provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBanned;

    private User(Long userId, String email, String password, String nickname, String imageUrl, Role role,
                 Provider provider, LocalDateTime createdAt,
                 LocalDateTime updatedAt, boolean isDeleted, boolean isBanned) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
        this.provider = provider;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.isBanned = isBanned;
    }

    private User(String email, String password, String nickname, String imageUrl) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
        this.provider = Provider.LOCAL;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.isBanned = false;
    }

    private User(String email, String nickname, String imageUrl, String password, Provider provider) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = Role.USER;
        this.provider = provider;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.isBanned = false;
    }

    public static User createUser(Long userId, String email, String password, String nickname, String imageUrl,
                                  Role role, Provider provider, LocalDateTime createdAt, LocalDateTime updatedAt,
                                  boolean isDeleted, boolean isBanned) {
        return new User(userId, email, password, nickname, imageUrl, role, provider, createdAt, updatedAt, isDeleted,
                isBanned);
    }

    public static User createUser(String email, String password, String nickname, String imageUrl) { //로컬 회원가입
        return new User(email, password, nickname, imageUrl);
    }

    public static User createKakaoUser(String email, String nickname, String imageUrl, String password) { //카카오 로그인
        return new User(email, nickname, imageUrl, password, Provider.KAKAO);
    }

    public org.springframework.security.core.userdetails.User toUserDetails() {
        return new org.springframework.security.core.userdetails.User(this.email, this.password,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

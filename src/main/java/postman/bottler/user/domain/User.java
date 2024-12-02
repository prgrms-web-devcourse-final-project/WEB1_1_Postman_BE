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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBanned;

    private User(Long userId, String email, String password, String nickname, String imageUrl, LocalDateTime createdAt,
                LocalDateTime updatedAt, boolean isDeleted, boolean isBanned) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.isBanned = isBanned;
    }

    private User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.isBanned = false;
    }

    public static User createUser(Long userId, String email, String password, String nickname, String imageUrl, LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted, boolean isBanned) {
        return new User(userId, email, password, nickname, imageUrl, createdAt, updatedAt, isDeleted, isBanned);
    }

    public static User createUser(String email, String password, String nickname) {
        return new User(email, password, nickname);
    }

    public org.springframework.security.core.userdetails.User toUserDetails() {
        return new org.springframework.security.core.userdetails.User(this.email, this.password, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}

package postman.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {
    private Long userId;
    private final String email;
    private String password;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBanned;

    private User(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public User(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.isDeleted = false;
        this.isBanned = false;
    }

    public static User createUser(Long userId, String email) {
        return new User(userId, email);
    }

    public static User createUser(String email, String password, String nickname) {
        return new User(email, password, nickname);
    }
}

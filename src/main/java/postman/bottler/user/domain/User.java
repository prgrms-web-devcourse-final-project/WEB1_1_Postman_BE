package postman.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class User {
    private static final int MAX_WARNING_COUNT = 3;

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
    private int warningCount;

    private User(Long userId, String email, String password, String nickname, String imageUrl, Role role,
                 Provider provider, LocalDateTime createdAt,
                 LocalDateTime updatedAt, boolean isDeleted, int warningCount) {
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
        this.warningCount = warningCount;
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
        this.warningCount = 0;
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
        this.warningCount = 0;
    }

    public static User createUser(Long userId, String email, String password, String nickname, String imageUrl,
                                  Role role, Provider provider, LocalDateTime createdAt, LocalDateTime updatedAt,
                                  boolean isDeleted, int warningCount) {
        return new User(userId, email, password, nickname, imageUrl, role, provider, createdAt, updatedAt, isDeleted,
                warningCount);
    }

    public static User createUser(String email, String password, String nickname, String imageUrl) { //로컬 회원가입
        return new User(email, password, nickname, imageUrl);
    }

    public static User createKakaoUser(String email, String nickname, String imageUrl, String password) { //카카오 로그인
        return new User(email, nickname, imageUrl, password, Provider.KAKAO);
    }

    public void updateWarningCount() {
        if (this.warningCount >= MAX_WARNING_COUNT) { //정지
            this.role = Role.BAN_USER;
            this.updatedAt = LocalDateTime.now();
            this.warningCount = 0;
        } else {
            this.warningCount++;
        }
    }

    public boolean isBanned() {
        return this.role == Role.BAN_USER;
    }
}

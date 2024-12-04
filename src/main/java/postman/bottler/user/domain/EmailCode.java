package postman.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import postman.bottler.user.exception.EmailCodeException;

@Getter
public class EmailCode {
    private static final long EXPIRATION_MINUTES = 5;

    private String email;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;

    private EmailCode(String email, String code) {
        this.email = email;
        this.code = code;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusMinutes(EXPIRATION_MINUTES);
    }

    private EmailCode(String email, String code, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.email = email;
        this.code = code;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public static EmailCode createEmailCode(String email, String authCode) {
        return new EmailCode(email, authCode);
    }

    public static EmailCode createEmailCode(String email, String authCode, LocalDateTime createdAt, LocalDateTime expiresAt) {
        return new EmailCode(email, authCode, createdAt, expiresAt);
    }

    public void checkExpiration() {
        if (LocalDateTime.now().isBefore(this.expiresAt)) {
            throw new EmailCodeException("유효시간이 지난 인증코드입니다.");
        }
    }
}

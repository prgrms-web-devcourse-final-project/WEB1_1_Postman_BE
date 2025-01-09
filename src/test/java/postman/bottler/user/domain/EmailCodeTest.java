package postman.bottler.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.user.exception.EmailCodeException;

@DisplayName("이메일 인증 코드 테스트")
class EmailCodeTest {
    private String testEmail;
    private String testCode;
    private static final long EXPIRATION_MINUTES = 5;


    @BeforeEach
    void setUp() {
        testEmail = "test@test.com";
        testCode = "123456";
    }

    @Test
    @DisplayName("인증코드 객체 생성 - createdAt과 expiresAt이 주어지지 않았을 경우 createdAt에서 5분을 더한 값이 expiresAt과 일치해야 한다.")
    public void createEmailCodeWithEmailAndCode() {
        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode);

        assertNotNull(emailCode);
        assertEquals(testEmail, emailCode.getEmail());
        assertEquals(testCode, emailCode.getCode());
        assertNotNull(emailCode.getCreatedAt());
        assertNotNull(emailCode.getExpiresAt());
        assertEquals(emailCode.getCreatedAt().plusMinutes(EXPIRATION_MINUTES), emailCode.getExpiresAt());
    }

    @Test
    @DisplayName("인증코드 객체 생성 - createdAt과 expiresAt이 주어진 경우 주어진 값이 저장되어야 한다.")
    public void createEmailCode() {
        LocalDateTime createdAt = LocalDateTime.of(2025, 1, 6, 10, 0, 0);
        LocalDateTime expiresAt = LocalDateTime.of(2025, 1, 6, 10, 30, 0);

        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode, createdAt, expiresAt);

        assertNotNull(emailCode);
        assertEquals(testEmail, emailCode.getEmail());
        assertEquals(testCode, emailCode.getCode());
        assertEquals(createdAt, emailCode.getCreatedAt());
        assertEquals(expiresAt, emailCode.getExpiresAt());
    }

    @Test
    @DisplayName("유효시간내 성공")
    public void checkExpiration() {
        LocalDateTime now = LocalDateTime.now();
        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode, now, now.plusMinutes(5));

        assertDoesNotThrow(emailCode::checkExpiration);
    }

    @Test
    @DisplayName("유효시간 초과")
    public void checkInvalidExpiration() {
        LocalDateTime now = LocalDateTime.now();
        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode, now, now.minusMinutes(1));

        EmailCodeException exception = assertThrows(EmailCodeException.class, emailCode::checkExpiration);
        assertEquals("유효시간이 지난 인증코드입니다.", exception.getMessage());
    }
}

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

    @BeforeEach
    void setUp() {
        testEmail = "test@test.com";
        testCode = "123456";
    }

    @Test
    @DisplayName("인증코드 객체 생성 - email, code")
    public void createEmailCodeWithEmailAndCode() {
        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode);

        assertNotNull(emailCode);
        assertEquals(testEmail, emailCode.getEmail());
        assertEquals(testCode, emailCode.getCode());
        assertNotNull(emailCode.getCreatedAt());
        assertNotNull(emailCode.getExpiresAt());
    }

    @Test
    @DisplayName("인증코드 객체 생성 - email, code, createdAt, expiresAt")
    public void createEmailCode() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiresAt = now.plusMinutes(5);

        EmailCode emailCode = EmailCode.createEmailCode(testEmail, testCode, now, expiresAt);

        assertNotNull(emailCode);
        assertEquals(testEmail, emailCode.getEmail());
        assertEquals(testCode, emailCode.getCode());
        assertEquals(now, emailCode.getCreatedAt());
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
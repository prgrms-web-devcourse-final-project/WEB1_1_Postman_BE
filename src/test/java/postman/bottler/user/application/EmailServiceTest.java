package postman.bottler.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import postman.bottler.user.application.service.EmailService;
import postman.bottler.user.exception.EmailException;

@DisplayName("이메일 인증 서비스 테스트")
class EmailServiceTest {
    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender emailSender;

    @Mock
    private MimeMessage mimeMessage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(emailSender.createMimeMessage()).thenReturn(mimeMessage);
        ReflectionTestUtils.setField(emailService, "username", "username@test.com");
    }

    @Test
    @DisplayName("정상적으로 인증 코드 이메일을 보낸다.")
    public void sendEmail() throws MessagingException {
        //given
        String toEmail = "test@test.com";
        String title = "이메일 제목";
        String content = "이메일 내용";

        //when
        emailService.sendEmail(toEmail, title, content);

        //then
        verify(emailSender).send(mimeMessage);
    }

    @Test
    @DisplayName("이메일 전송 중 예외가 발생한다.")
    public void sendEmailThrowException() {
        //given
        String toEmail = "test@test.com";
        String title = "이메일 제목";
        String content = "이메일 내용";

        doThrow(RuntimeException.class).when(emailSender).send(mimeMessage);

        //when & then
        assertThatThrownBy(() -> emailService.sendEmail(toEmail, title, content))
                .isInstanceOf(EmailException.class)
                .hasMessage("이메일 인증 요청에 실패했습니다.");
    }
}

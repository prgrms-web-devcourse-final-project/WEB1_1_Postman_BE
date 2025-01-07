package postman.bottler.user.application;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import postman.bottler.user.application.service.KakaoService;

@DisplayName("카카오 로그인 서비스 테스트")
class KakaoServiceTest {
    @InjectMocks
    private KakaoService kakaoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("request URL을 생성한다.")
    public void getRequestURL() {
        //given
        String clientId = "testClientId";
        String redirectUrl = "testRedirectUrl";

        ReflectionTestUtils.setField(kakaoService, "KAKAO_CLIENT_ID", clientId);
        ReflectionTestUtils.setField(kakaoService, "KAKAO_REDIRECT_URL", redirectUrl);

        //when
        String result = kakaoService.getRequestURL();

        //then
        String expectedUrl = "https://kauth.kakao.com/oauth/authorize?response_type=code"
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUrl;
        assertThat(result).isEqualTo(expectedUrl);
    }
}

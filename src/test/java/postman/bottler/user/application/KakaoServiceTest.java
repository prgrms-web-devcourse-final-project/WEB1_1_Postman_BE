package postman.bottler.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import postman.bottler.user.application.service.KakaoService;

@DisplayName("카카오 로그인 서비스 테스트")
class KakaoServiceTest {
    @InjectMocks
    private KakaoService kakaoService;

    @Mock
    private RestTemplate restTemplate;

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

    @Test
    @DisplayName("정상적으로 카카오 액세스 토큰을 가져온다.")
    public void getKakaoAccessToken() {
//        //given
//        String code = "testCode";
//        String accessToken = "testAccessToken";
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("access_token", accessToken);
//
//        ResponseEntity<String> responseEntity = ResponseEntity.ok(jsonObject.toString());
//
//        when(restTemplate.exchange(
//                eq("https://kauth.kakao.com/oauth/token"),
//                eq(HttpMethod.POST),
//                any(HttpEntity.class),
//                eq(String.class)
//        )).thenReturn(responseEntity);
//
//        //when
//        String result = kakaoService.getKakaoAccessToken(code);
//
//        //then
//        assertThat(result).isEqualTo(accessToken);
    }


    @Test
    @DisplayName("카카오 액세스 토큰 요청 시 응답을 받을 수 없다.")
    public void getKakaoAccessTokenThrowException_NoResponse() {

    }

    @Test
    @DisplayName("카카오 액세스 토큰 요청 시 잘못된 인증 토큰이다.")
    public void getKakaoAccessTokenThrowException_InvalidToken() {

    }

    @Test
    @DisplayName("카카오 액세스 토큰 요청 시 인증 과정에서 오류가 발생했다.")
    public void getKakaoAccessTokenThrowException_Error() {

    }
}
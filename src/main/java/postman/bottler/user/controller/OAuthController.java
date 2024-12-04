package postman.bottler.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.exception.KakaoAuthCodeException;
import postman.bottler.user.service.KakaoService;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth")
@Tag(name = "소셜로그인", description = "소셜로그인 관련 API")
public class OAuthController {
    private final KakaoService kakaoService;
    private final UserService userService;

    @Operation(summary = "카카오 소셜로그인", description = "카카오 서버로 요청을 보내 회원가입 및 로그인을 합니다.")
    @GetMapping("/kakao")
    public RedirectView kakaoCode() {
        return new RedirectView(kakaoService.getRequestURL());
    }

    @GetMapping("/kakao/token")
    public ApiResponse<SignInResponseDTO> kakaoSignin(@RequestParam("code") String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new KakaoAuthCodeException("인가 코드가 비어있습니다.");
        }
        String accessToken = kakaoService.getKakaoAccessToken(code);
        Map<String, String> userInfo = kakaoService.getUserInfo(accessToken);
        String kakaoId = userInfo.get("kakaoId");
        String nickname = userInfo.get("nickname");

        SignInResponseDTO signInResponseDTO = userService.kakaoSignin(kakaoId, nickname);
        return ApiResponse.onSuccess(signInResponseDTO);
    }
}

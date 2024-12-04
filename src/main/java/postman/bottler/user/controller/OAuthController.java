package postman.bottler.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.KakaoRequestDTO;
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

    @Operation(summary = "카카오 소셜로그인", description = "인가코드로 유저정보를 받아와 회원가입 및 로그인을 합니다.")
    @PostMapping("/kakao")
    public ApiResponse<SignInResponseDTO> kakaoSignin(@Valid @RequestBody KakaoRequestDTO kakaoRequestDTO,
                                                      BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new KakaoAuthCodeException(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }

        String accessToken = kakaoService.getKakaoAccessToken(kakaoRequestDTO.code());
        Map<String, String> userInfo = kakaoService.getUserInfo(accessToken);
        String kakaoId = userInfo.get("kakaoId");
        String nickname = userInfo.get("nickname");

        SignInResponseDTO signInResponseDTO = userService.kakaoSignin(kakaoId, nickname);
        return ApiResponse.onSuccess(signInResponseDTO);
    }
}

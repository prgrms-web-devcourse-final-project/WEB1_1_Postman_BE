package postman.bottler.user.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.auth.CookieService;
import postman.bottler.user.application.dto.request.SignInRequestDTO;
import postman.bottler.user.application.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.application.dto.response.SignInDTO;
import postman.bottler.user.application.dto.response.SignInResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.TokenException;
import postman.bottler.user.application.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "유저", description = "유저 관련 API")
public class AuthController {
    private final UserService userService;
    private final CookieService cookieService;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/signin")
    public ApiResponse<SignInResponseDTO> signin(
            @Valid @RequestBody SignInRequestDTO signInRequestDTO,
            BindingResult bindingResult,
            HttpServletResponse response) {
        validateRequestDTO(bindingResult);
        SignInDTO signInDTO = userService.signin(signInRequestDTO.email(), signInRequestDTO.password());
        cookieService.addCookie(response, "refreshToken", signInDTO.refreshToken());
        return ApiResponse.onSuccess(new SignInResponseDTO(signInDTO.accessToken()));
    }

    @Operation(summary = "리프레시 토큰 유효성 검사", description = "리프레시 토큰 유효성 검사 성공 시 새로운 액세스 토큰 발급합니다.")
    @PostMapping("/validate")
    public ApiResponse<AccessTokenResponseDTO> validateRefreshToken(HttpServletRequest request) {
        String refreshToken = getCookieValue(request);
        if (refreshToken == null) {
            throw new TokenException("Refresh token이 존재하지 않습니다.");
        }

        AccessTokenResponseDTO newAccessToken = userService.validateRefreshToken(refreshToken);
        return ApiResponse.onSuccess(newAccessToken);
    }

    private String getCookieValue(HttpServletRequest request) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password" -> throw new PasswordException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}

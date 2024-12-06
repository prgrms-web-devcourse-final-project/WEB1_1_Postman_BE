package postman.bottler.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.user.dto.request.RefreshTokenRequestDTO;
import postman.bottler.user.dto.request.SignInRequestDTO;
import postman.bottler.user.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.dto.response.SignInDTO;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.TokenException;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "유저", description = "유저 관련 API")
public class AuthController {
    private final UserService userService;

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
    @PostMapping("/signin")
    public ApiResponse<SignInResponseDTO> signin(
            @Valid @RequestBody SignInRequestDTO signInRequestDTO,
            BindingResult bindingResult,
            HttpServletResponse response) {
        validateRequestDTO(bindingResult);
        SignInDTO signInDTO = userService.signin(signInRequestDTO.email(), signInRequestDTO.password());
        addHttpOnlyCookie(response, "refreshToken", signInDTO.refreshToken());

        return ApiResponse.onSuccess(new SignInResponseDTO(signInDTO.accessToken()));
    }

    private void addHttpOnlyCookie(HttpServletResponse response, String name, String value) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        response.addCookie(cookie);
    }

    @Operation(summary = "리프레시 토큰 유효성 검사", description = "리프레시 토큰 유효성 검사 성공 시 새로운 액세스 토큰 발급합니다.")
    @PostMapping("/validate")
    public ApiResponse<AccessTokenResponseDTO> validateRefreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO, BindingResult bindingResult) {
        validateRequestDTO(bindingResult);
        AccessTokenResponseDTO newAccessToken = userService.validateRefreshToken(refreshTokenRequestDTO.refreshToken());
        return ApiResponse.onSuccess(newAccessToken);
    }

    private void validateRequestDTO(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "email" -> throw new EmailException(error.getDefaultMessage());
                    case "password" -> throw new PasswordException(error.getDefaultMessage());
                    case "refreshToken" -> throw new TokenException(error.getDefaultMessage());
                    default -> throw new IllegalArgumentException(
                            bindingResult.getAllErrors().get(0).getDefaultMessage());
                }
            });
        }
    }
}

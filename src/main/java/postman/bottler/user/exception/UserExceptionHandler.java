package postman.bottler.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(EmailException.class)
    public ApiResponse<?> handleEmailException(EmailException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(PasswordException.class)
    public ApiResponse<?> handlePasswordException(PasswordException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_PASSWORD.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NicknameException.class)
    public ApiResponse<?> handleNicknameException(NicknameException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_NICKNAME.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(SignInException.class)
    public ApiResponse<?> handleSignInException(SignInException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL_AND_PASSWORD.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(TokenException.class)
    public ApiResponse<?> handleTokenException(TokenException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_TOKEN_INPUT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(UserException.class)
    public ApiResponse<?> handleUserException(UserException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_USER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(ProfileImageException.class)
    public ApiResponse<?> handleProfileImageException(ProfileImageException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_PROFILE_IMAGE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmailCodeException.class)
    public ApiResponse<?> handleEmailCodeException(EmailCodeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL_CODE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(KakaoAuthCodeException.class)
    public ApiResponse<?> handleKakaoAuthCodeException(KakaoAuthCodeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_KAKAO_CODE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(KakaoException.class)
    public ApiResponse<?> handleKakaoException(KakaoException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.FAILED_KAKAO_SIGNIN.getCode(), e.getMessage(), null);
    }
}

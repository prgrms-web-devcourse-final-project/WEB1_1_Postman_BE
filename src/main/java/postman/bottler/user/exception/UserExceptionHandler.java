package postman.bottler.user.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class UserExceptionHandler {
    @ExceptionHandler(EmailException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmailException(EmailException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(PasswordException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handlePasswordException(PasswordException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_PASSWORD.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(NicknameException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleNicknameException(NicknameException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_NICKNAME.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(SignInException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleSignInException(SignInException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL_AND_PASSWORD.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(TokenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleTokenException(TokenException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_TOKEN_INPUT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(UserException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserException(UserException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_USER.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(ProfileImageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleProfileImageException(ProfileImageException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_PROFILE_IMAGE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmailCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmailCodeException(EmailCodeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_EMAIL_CODE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(KakaoAuthCodeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleKakaoAuthCodeException(KakaoAuthCodeException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.INVALID_KAKAO_CODE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(KakaoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleKakaoException(KakaoException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.FAILED_KAKAO_SIGNIN.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(UserBanException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleUserBanException(UserBanException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_USER_BAN.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(SingUpException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleSingUpException(SingUpException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DUPLICATE_EMAIL_OR_NICKNAME.getCode(), e.getMessage(), null);
    }
}

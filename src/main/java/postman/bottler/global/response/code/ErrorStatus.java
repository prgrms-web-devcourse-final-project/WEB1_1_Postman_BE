package postman.bottler.global.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 알림 에러
    INVALID_NOTIFICATION_CREATION(HttpStatus.BAD_REQUEST, "NOTIFICATION4000"),
    NOTIFICATION_NO_LETTER(HttpStatus.BAD_REQUEST, "NOTIFICATION4001"),
    NOTIFICATION_NO_TYPE(HttpStatus.BAD_REQUEST, "NOTIFICATION4002"),

    // 공통 에러
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "COMMON400"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403");

    private final HttpStatus httpStatus;
    private final String code;
}

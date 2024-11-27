package postman.bottler.global.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 공통 에러
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "COMMON400"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403"),

    // 라벨 에러
    INVALID_LABEL(HttpStatus.BAD_REQUEST, "LABEL4000"),
    EMPTY_LABEL_INPUT(HttpStatus.BAD_REQUEST, "LABEL4001"),
    USER_LABEL_NOT_FOUND(HttpStatus.NOT_FOUND, "LABEL4002"),
    OVER_LIMIT_COUNT(HttpStatus.FORBIDDEN, "LABEL4003");

    private final HttpStatus httpStatus;
    private final String code;
}

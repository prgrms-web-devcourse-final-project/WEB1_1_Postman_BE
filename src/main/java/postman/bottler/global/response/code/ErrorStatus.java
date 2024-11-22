package postman.bottler.global.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 공통 에러
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "COMMON400"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403");

    private final HttpStatus httpStatus;
    private final String code;
}

package postman.bottler.global.response.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus {
    // 공통 에러
    INVALID_LOGIN(HttpStatus.BAD_REQUEST, "COMMON400"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403"),

    // 키워드 편지 에러
    LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "LETTER4040"),
    LETTER_ALREADY_SAVED(HttpStatus.BAD_REQUEST, "LETTER4001");

    private final HttpStatus httpStatus;
    private final String code;
}

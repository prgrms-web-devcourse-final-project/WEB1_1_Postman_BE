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

    // 지도 편지 에러
    MAP_LETTER_NOT_FOUND(HttpStatus.NOT_FOUND, "MAP4000"),
    LOCATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "MAP4001"),
    EMPTY_MAP_LETTER_CONTENT(HttpStatus.BAD_REQUEST, "MAP4002"),
    EMPTY_MAP_LETTER_TITLE(HttpStatus.BAD_REQUEST, "MAP4003"),
    EMPTY_MAP_LETTER_TARGET(HttpStatus.BAD_REQUEST, "MAP4004"),
    MAP_LETTER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "MAP4005"),
    EMPTY_MAP_LETTER_DESCRIPTION(HttpStatus.BAD_REQUEST, "MAP4006"),
    EMPTY_REPLY_MAP_LETTER_SOURCE(HttpStatus.BAD_REQUEST, "MAP4007"),
    SOURCE_MAP_LETTER_NOT_FOUND(HttpStatus.BAD_REQUEST, "MAP4008"),
    LETTER_ALREADY_ARCHIVED(HttpStatus.BAD_REQUEST, "MAP4009"),
    LETTER_ALREADY_REPLY(HttpStatus.BAD_REQUEST, "MAP40010"),

    ;


    private final HttpStatus httpStatus;
    private final String code;
}

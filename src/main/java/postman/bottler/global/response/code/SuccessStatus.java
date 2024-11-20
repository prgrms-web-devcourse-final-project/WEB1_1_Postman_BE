package postman.bottler.global.response.code;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SuccessStatus {
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),
    CREATE_SUCCESS(HttpStatus.CREATED, "COMMON201", "생성에 성공했습니다."),
    DELETE_SUCCESS(HttpStatus.OK, "COMMON202", "삭제에 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}

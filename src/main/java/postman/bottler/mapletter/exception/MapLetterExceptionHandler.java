package postman.bottler.mapletter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class MapLetterExceptionHandler {

    @ExceptionHandler(EmptyMapLetterContentException.class)
    public ApiResponse<?> handleEmptyLetterContentException(EmptyMapLetterContentException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_CONTENT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterTitleException.class)
    public ApiResponse<?> handleEmptyMapLetterTitleException(EmptyMapLetterTitleException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_TITLE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterTargetException.class)
    public ApiResponse<?> handleEmptyMapLetterTargetException(EmptyMapLetterTargetException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_TARGET.getCode(), e.getMessage(), null);
    }
}

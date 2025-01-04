package postman.bottler.mapletter.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.global.response.code.ErrorStatus;

@RestControllerAdvice
@Slf4j
public class MapLetterExceptionHandler {

    @ExceptionHandler(EmptyMapLetterContentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyLetterContentException(EmptyMapLetterContentException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_CONTENT.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterTitleException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyMapLetterTitleException(EmptyMapLetterTitleException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_TITLE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterTargetException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyMapLetterTargetException(EmptyMapLetterTargetException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_TARGET.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MapLetterNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMapLetterNotFoundException(MapLetterNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.MAP_LETTER_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MapLetterAlreadyDeletedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMapLetterAlreadyDeletedException(MapLetterAlreadyDeletedException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.MAP_LETTER_ALREADY_DELETED.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleLocationNotFoundException(LocationNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LOCATION_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterDescriptionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyMapLetterDescriptionException(EmptyMapLetterDescriptionException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_DESCRIPTION.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyReplyMapLetterSourceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleEmptyReplyMapLetterSourceException(EmptyReplyMapLetterSourceException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_REPLY_MAP_LETTER_SOURCE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(SourceMapLetterNotFountException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleSourceMapLetterNotFountException(SourceMapLetterNotFountException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.SOURCE_MAP_LETTER_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MapLetterAlreadyArchivedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleMapLetterAlreadyArchivedException(MapLetterAlreadyArchivedException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_ALREADY_ARCHIVED.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(LetterAlreadyReplyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleLetterAlreadyReplyException(LetterAlreadyReplyException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_ALREADY_REPLY.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DistanceToFarException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleDistanceToFarException(DistanceToFarException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DISTANCE_TOO_FAR.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(BlockedLetterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handleBlockedLetterException(BlockedLetterException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_BLOCKED.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(PageRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<?> handlePageRequestException(PageRequestException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.PAGINATION_VALIDATION_ERROR.getCode(), e.getMessage(), null);
    }
}

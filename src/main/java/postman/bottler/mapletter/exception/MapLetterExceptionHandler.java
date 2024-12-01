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

    @ExceptionHandler(MapLetterNotFoundException.class)
    public ApiResponse<?> handleMapLetterNotFoundException(MapLetterNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.MAP_LETTER_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MapLetterAlreadyDeletedException.class)
    public ApiResponse<?> handleMapLetterAlreadyDeletedException(MapLetterAlreadyDeletedException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.MAP_LETTER_ALREADY_DELETED.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public ApiResponse<?> handleLocationNotFoundException(LocationNotFoundException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LOCATION_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyMapLetterDescriptionException.class)
    public ApiResponse<?> handleEmptyMapLetterDescriptionException(EmptyMapLetterDescriptionException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_MAP_LETTER_DESCRIPTION.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(EmptyReplyMapLetterSourceException.class)
    public ApiResponse<?> handleEmptyReplyMapLetterSourceException(EmptyReplyMapLetterSourceException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.EMPTY_REPLY_MAP_LETTER_SOURCE.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(SourceMapLetterNotFountException.class)
    public ApiResponse<?> handleSourceMapLetterNotFountException(SourceMapLetterNotFountException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.SOURCE_MAP_LETTER_NOT_FOUND.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(MapLetterAlreadyArchivedException.class)
    public ApiResponse<?> handleMapLetterAlreadyArchivedException(MapLetterAlreadyArchivedException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_ALREADY_ARCHIVED.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(LetterAlreadyReplyException.class)
    public ApiResponse<?> handleLetterAlreadyReplyException(LetterAlreadyReplyException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_ALREADY_REPLY.getCode(), e.getMessage(), null);
    }

    @ExceptionHandler(DistanceToFarException.class)
    public ApiResponse<?> handleDistanceToFarException(DistanceToFarException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.DISTANCE_TOO_FAR.getCode(), e.getMessage(), null);
    }
    @ExceptionHandler(BlockedLetterException.class)
    public ApiResponse<?> handleBlockedLetterException(BlockedLetterException e) {
        log.error(e.getMessage());
        return ApiResponse.onFailure(ErrorStatus.LETTER_BLOCKED.getCode(), e.getMessage(), null);
    }
}

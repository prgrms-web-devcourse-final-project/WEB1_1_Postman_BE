package postman.bottler.letter.controller;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.request.LetterDeleteRequestDTO;
import postman.bottler.letter.dto.request.PageRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.exception.InvalidPageRequestException;
import postman.bottler.letter.service.DeleteManagerService;
import postman.bottler.letter.service.LetterBoxService;

@Slf4j
@RestController
@RequestMapping("/letters/saved")
@RequiredArgsConstructor
public class LetterBoxController {

    private final LetterBoxService letterBoxService;
    private final DeleteManagerService deleteManagerService;

    @GetMapping
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getAllLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult
    ) {
        validatePageRequest(bindingResult);
        Page<LetterHeadersResponseDTO> result = letterBoxService.getAllLetterHeaders(pageRequestDTO);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/sent")
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getSentLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult
    ) {
        validatePageRequest(bindingResult);
        Page<LetterHeadersResponseDTO> result = letterBoxService.getSentLetterHeaders(pageRequestDTO);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/received")
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getReceivedLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult
    ) {
        validatePageRequest(bindingResult);
        Page<LetterHeadersResponseDTO> result = letterBoxService.getReceivedLetterHeaders(pageRequestDTO);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @DeleteMapping
    public ApiResponse<String> deleteSavedLetter(
            @RequestBody @Valid List<LetterDeleteRequestDTO> letterDeleteRequestDTOS
    ) {
        deleteManagerService.deleteLetters(letterDeleteRequestDTOS);
        return ApiResponse.onSuccess("편지 보관을 취소했습니다.");
    }

    private static void validatePageRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "검증 실패")
                    ));
            throw new InvalidPageRequestException("유효성 검사 실패", errors);
        }
    }
}

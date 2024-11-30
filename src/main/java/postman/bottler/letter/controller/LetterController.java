package postman.bottler.letter.controller;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.exception.InvalidLetterRequestException;
import postman.bottler.letter.service.LetterService;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @PostMapping
    public ApiResponse<LetterResponseDTO> createLetter(@RequestBody @Valid LetterRequestDTO letterRequestDTO,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "검증 실패")
                    ));
            throw new InvalidLetterRequestException("유효성 검사 실패", errors);
        }

        LetterResponseDTO result = letterService.createLetter(letterRequestDTO);
        return ApiResponse.onCreateSuccess(result);
    }

    @GetMapping
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getLetterHeaders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<LetterHeadersResponseDTO> result = letterService.getLetterHeaders(page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @DeleteMapping("/{letterId}")
    public ApiResponse<String> deleteLetter(@PathVariable Long letterId) {
        letterService.deleteLetter(letterId);
        return ApiResponse.onSuccess("키워드 편지를 삭제했습니다.");
    }

    @GetMapping("/detail/{letterId}")
    public ApiResponse<LetterDetailResponseDTO> getLetter(@PathVariable Long letterId) {
        LetterDetailResponseDTO result = letterService.getLetterDetail(letterId);
        return ApiResponse.onSuccess(result);
    }
}

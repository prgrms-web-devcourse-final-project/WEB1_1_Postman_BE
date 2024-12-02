package postman.bottler.letter.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.request.LetterDeleteRequestDTO;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.InvalidLetterRequestException;
import postman.bottler.letter.service.DeleteManagerService;
import postman.bottler.letter.service.LetterService;
import postman.bottler.letter.utiil.ValidationUtil;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;
    private final DeleteManagerService deleteManagerService;
    private final ValidationUtil validationUtil;

    @PostMapping
    public ApiResponse<LetterResponseDTO> createLetter(
            @RequestBody @Valid LetterRequestDTO letterRequestDTO, BindingResult bindingResult
    ) {
        validateLetterRequest(bindingResult);
        LetterResponseDTO result = letterService.createLetter(letterRequestDTO);
        return ApiResponse.onCreateSuccess(result);
    }

    @DeleteMapping("/{letterId}")
    public ApiResponse<String> deleteLetter(
            @RequestBody @Valid LetterDeleteRequestDTO letterDeleteRequestDTO
    ) {
        deleteManagerService.deleteLetters(List.of(letterDeleteRequestDTO));
        return ApiResponse.onSuccess("키워드 편지를 삭제했습니다.");
    }

    @GetMapping("/detail/{letterId}")
    public ApiResponse<LetterDetailResponseDTO> getLetter(@PathVariable Long letterId) {
        LetterDetailResponseDTO result = letterService.getLetterDetail(letterId);
        return ApiResponse.onSuccess(result);
    }

    private void validateLetterRequest(BindingResult bindingResult) {
        validationUtil.validate(bindingResult,
                errors -> new InvalidLetterRequestException("유효성 검사 실패", errors));
    }
}

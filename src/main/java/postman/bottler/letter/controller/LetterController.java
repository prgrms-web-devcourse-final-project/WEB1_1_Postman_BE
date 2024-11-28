package postman.bottler.letter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.service.LetterService;
import postman.bottler.letter.service.SavedLetterService;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;
    private final SavedLetterService savedLetterService;

    @PostMapping
    public ApiResponse<LetterResponseDTO> createLetter(@RequestBody @Valid LetterRequestDTO letterRequestDTO) {
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
    public ApiResponse<LetterResponseDTO> getLetter(@PathVariable Long letterId) {
        LetterResponseDTO result = letterService.getLetterDetail(letterId);
        return ApiResponse.onSuccess(result);
    }

    @PutMapping("/{letterId}/save")
    public ApiResponse<String> saveLetter(@PathVariable Long letterId) {
        savedLetterService.saveLetter(letterId);
        return ApiResponse.onSuccess("키워드 편지를 보관했습니다.");
    }

    @GetMapping("/saved")
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getSavedLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size
    ) {
        Page<LetterHeadersResponseDTO> result = savedLetterService.getSavedLetterHeaders(page, size);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @DeleteMapping("/saved/{letterId}")
    public ApiResponse<String> deleteSavedLetter(@PathVariable Long letterId) {
        savedLetterService.deleteSavedLetter(letterId);
        return ApiResponse.onSuccess("키워드 편지 보관을 취소했습니다.");
    }
}

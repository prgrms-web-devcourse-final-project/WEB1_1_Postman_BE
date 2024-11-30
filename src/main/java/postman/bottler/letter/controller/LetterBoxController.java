package postman.bottler.letter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.service.LetterBoxService;

@RestController
@RequestMapping("/letters/saved")
@RequiredArgsConstructor
public class LetterBoxController {

    private final LetterBoxService letterBoxService;

    @GetMapping
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getAllLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<LetterHeadersResponseDTO> result = letterBoxService.getAllLetterHeaders(page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/sent")
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getSentLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<LetterHeadersResponseDTO> result = letterBoxService.getSentLetterHeaders(page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/received")
    public ApiResponse<PageResponseDTO<LetterHeadersResponseDTO>> getReceivedLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<LetterHeadersResponseDTO> result = letterBoxService.getReceivedLetterHeaders(page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @DeleteMapping("/saved/{letterId}")
    public ApiResponse<String> deleteSavedLetter(@PathVariable Long letterId) {
        letterBoxService.handleLetterDeletion(letterId);
        return ApiResponse.onSuccess("편지 보관을 취소했습니다.");
    }
}

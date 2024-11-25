package postman.bottler.letter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.service.LetterService;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
public class LetterController {

    private final LetterService letterService;

    @PostMapping
    public ApiResponse<LetterResponseDTO> createLetter(@RequestBody LetterRequestDTO letterRequestDTO) {
        LetterResponseDTO result = letterService.createLetter(letterRequestDTO);
        return ApiResponse.onCreateSuccess(result);
    }

    @GetMapping("/sent")
    public ApiResponse<Page<LetterKeywordsResponseDTO>> getLetterKeywords(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdDate") String sort
    ) {
        Page<LetterKeywordsResponseDTO> result = letterService.getLetterKeywords(page, size, sort);
        return ApiResponse.onSuccess(result);
    }

    @DeleteMapping("/{letterId}")
    public ApiResponse<String> deleteLetter(@PathVariable Long letterId) {
        letterService.deleteLetter(letterId);
        return ApiResponse.onSuccess("success");
    }
}

package postman.bottler.letter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.application.dto.LetterDeleteDTO;
import postman.bottler.letter.application.dto.request.LetterDeleteRequestDTO;
import postman.bottler.letter.application.dto.request.LetterRequestDTO;
import postman.bottler.letter.application.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.application.dto.response.LetterRecommendSummaryResponseDTO;
import postman.bottler.letter.application.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.InvalidLetterRequestException;
import postman.bottler.letter.application.service.LetterDeletionService;
import postman.bottler.letter.application.service.LetterFacadeService;
import postman.bottler.letter.utiil.ValidationUtil;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequestMapping("/letters")
@RequiredArgsConstructor
@Tag(name = "키워드 편지", description = "키워드 편지 API")
public class LetterController {

    private final LetterDeletionService letterDeletionService;
    private final LetterFacadeService letterFacadeService;
    private final ValidationUtil validationUtil;

    @Operation(
            summary = "키워드 편지 생성",
            description = "새로운 키워드 편지를 생성합니다."
    )
    @PostMapping
    public ApiResponse<LetterResponseDTO> createLetter(
            @RequestBody @Valid LetterRequestDTO letterRequestDTO, BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validateLetterRequest(bindingResult);
        LetterResponseDTO result = letterFacadeService.createLetter(letterRequestDTO, userDetails.getUserId());
        return ApiResponse.onCreateSuccess(result);
    }

    @Operation(
            summary = "키워드 편지 상세 조회",
            description = "편지 ID로 키워드 편지의 상세 정보를 조회합니다."
    )
    @GetMapping("/detail/{letterId}")
    public ApiResponse<LetterDetailResponseDTO> getLetterDetail(
            @PathVariable Long letterId, @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        LetterDetailResponseDTO result = letterFacadeService.findLetterDetail(letterId, userDetails.getUserId());
        return ApiResponse.onSuccess(result);
    }


    @Operation(
            summary = "추천 키워드 편지 조회",
            description = "사용자에게 현재 추천된 키워드 편지들의 정보를 제공합니다."
    )
    @GetMapping("/recommend")
    public ApiResponse<List<LetterRecommendSummaryResponseDTO>> getRecommendLetters(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        List<LetterRecommendSummaryResponseDTO> result = letterFacadeService.findRecommendHeaders(
                userDetails.getUserId());
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "키워드 편지 삭제",
            description = "키워드 편지ID, BoxType 송수신(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다."
    )
    @DeleteMapping
    public ApiResponse<String> deleteLetter(
            @RequestBody @Valid LetterDeleteRequestDTO letterDeleteRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        letterDeletionService.deleteLetter(LetterDeleteDTO.fromLetter(letterDeleteRequestDTO), userDetails.getUserId());
        return ApiResponse.onSuccess("키워드 편지를 삭제했습니다.");
    }

    private void validateLetterRequest(BindingResult bindingResult) {
        validationUtil.validate(bindingResult,
                errors -> new InvalidLetterRequestException("유효성 검사 실패", errors));
    }
}
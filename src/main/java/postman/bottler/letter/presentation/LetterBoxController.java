package postman.bottler.letter.presentation;

import static postman.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.application.dto.LetterDeleteDTO;
import postman.bottler.letter.application.dto.request.PageRequestDTO;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.application.dto.response.PageResponseDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterDeletionService;
import postman.bottler.letter.presentation.annotation.LetterValidationMetaData;
import postman.bottler.user.auth.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/letters/saved")
@RequiredArgsConstructor
@Tag(name = "Letter Box", description = "보관된(saved) 편지 관리 API")
public class LetterBoxController {

    private final LetterBoxService letterBoxService;
    private final LetterDeletionService letterDeletionService;

    @Operation(
            summary = "보관된 모든 편지 조회",
            description = "페이지네이션을 사용하여 보관된 모든 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getAllLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<LetterSummaryResponseDTO> result = letterBoxService.findAllLetterSummaries(pageRequestDTO,
                userDetails.getUserId());
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "보낸 편지 조회",
            description = "페이지네이션을 사용하여 보관된 보낸 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping("/sent")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getSentLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<LetterSummaryResponseDTO> result = letterBoxService.findSentLetterSummaries(pageRequestDTO,
                userDetails.getUserId());
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "받은 편지 조회",
            description = "페이지네이션을 사용하여 보관된 받은 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping("/received")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getReceivedLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Page<LetterSummaryResponseDTO> result = letterBoxService.findReceivedLetterSummaries(pageRequestDTO,
                userDetails.getUserId());
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "보관된 편지 삭제",
            description = "편지ID, 편지타입(LETTER, REPLY_LETTER), 송수신 타입(SEND, RECEIVE)을 기반으로 키워드 편지를 삭제합니다."
    )
    @DeleteMapping
    public ApiResponse<String> deleteSavedLetter(
            @RequestBody @Valid List<LetterDeleteDTO> letterDeleteDTOS,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        letterDeletionService.deleteLetters(letterDeleteDTOS, userDetails.getUserId());
        return ApiResponse.onSuccess("편지 보관을 취소했습니다.");
    }
}

package postman.bottler.letter.controller;

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
import postman.bottler.letter.dto.LetterDeleteDTO;
import postman.bottler.letter.dto.request.PageRequestDTO;
import postman.bottler.letter.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.exception.InvalidPageRequestException;
import postman.bottler.letter.service.LetterBoxService;
import postman.bottler.letter.service.LetterDeletionService;
import postman.bottler.letter.utiil.ValidationUtil;
import postman.bottler.user.auth.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/letters/saved")
@RequiredArgsConstructor
@Tag(name = "Letter Box", description = "보관된(saved) 편지 관리 API")
public class LetterBoxController {

    private final LetterBoxService letterBoxService;
    private final LetterDeletionService letterDeletionService;
    private final ValidationUtil validationUtil;

    @Operation(
            summary = "보관된 모든 편지 조회",
            description = "페이지네이션을 사용하여 보관된 모든 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getAllLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validatePageRequest(bindingResult);
        Page<LetterSummaryResponseDTO> result = letterBoxService.getAllLetterHeaders(pageRequestDTO,
                userDetails.getUserId());
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "보낸 편지 조회",
            description = "페이지네이션을 사용하여 보관된 보낸 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping("/sent")
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getSentLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validatePageRequest(bindingResult);
        Page<LetterSummaryResponseDTO> result = letterBoxService.getSentLetterHeaders(pageRequestDTO,
                userDetails.getUserId());
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "받은 편지 조회",
            description = "페이지네이션을 사용하여 보관된 받은 편지의 제목, 라벨이미지, 작성날짜 정보를 조회합니다."
                    + "\nPage Default: page(1) size(9) sort(createAt)"
    )
    @GetMapping("/received")
    public ApiResponse<PageResponseDTO<LetterSummaryResponseDTO>> getReceivedLetters(
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        validatePageRequest(bindingResult);
        Page<LetterSummaryResponseDTO> result = letterBoxService.getReceivedLetterHeaders(pageRequestDTO,
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

    private void validatePageRequest(BindingResult bindingResult) {
        validationUtil.validate(bindingResult,
                errors -> new InvalidPageRequestException("유효성 검사 실패", errors));
    }
}

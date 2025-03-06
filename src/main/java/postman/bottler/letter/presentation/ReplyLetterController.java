package postman.bottler.letter.presentation;

import static postman.bottler.global.response.code.ErrorStatus.PAGINATION_VALIDATION_ERROR;
import static postman.bottler.global.response.code.ErrorStatus.REPLY_LETTER_VALIDATION_ERROR;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import postman.bottler.letter.application.dto.request.PageRequestDTO;
import postman.bottler.letter.application.dto.request.ReplyLetterDeleteRequestDTO;
import postman.bottler.letter.application.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.application.dto.response.PageResponseDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterDetailResponseDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterSummaryResponseDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.letter.application.service.LetterDeletionService;
import postman.bottler.letter.application.service.ReplyLetterService;
import postman.bottler.letter.presentation.annotation.LetterValidationMetaData;
import postman.bottler.user.auth.CustomUserDetails;

@Slf4j
@RestController
@RequestMapping("/letters/replies")
@RequiredArgsConstructor
@Tag(name = "Reply Letters", description = "키워드 편지 API")
public class ReplyLetterController {

    private final ReplyLetterService letterReplyService;
    private final LetterDeletionService letterDeletionService;
    private final LetterBoxService letterBoxService;

    @Operation(summary = "키워드 편지 생성", description = "지정된 편지 ID에 대한 답장을 생성합니다.")
    @PostMapping("/{letterId}")
    @LetterValidationMetaData(message = "키워드 답장 편지 유효성 검사 실패", errorStatus = REPLY_LETTER_VALIDATION_ERROR)
    public ApiResponse<ReplyLetterResponseDTO> createReplyLetter(@PathVariable Long letterId,
                                                                 @RequestBody @Valid ReplyLetterRequestDTO letterReplyRequestDTO,
                                                                 BindingResult bindingResult,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onCreateSuccess(
                letterReplyService.createReplyLetter(letterId, letterReplyRequestDTO, userDetails.getUserId()));
    }

    @Operation(summary = "특정 키워드 편지에 대한 답장 목록 조회", description = "지정된 편지 ID에 대한 답장들의 제목, 라벨이미지, 작성날짜를 페이지네이션 형태로 반환합니다."
            + "\nPage Default: page(1) size(9) sort(createAt)")
    @GetMapping("/{letterId}")
    @LetterValidationMetaData(message = "페이지네이션 유효성 검사 실패", errorStatus = PAGINATION_VALIDATION_ERROR)
    public ApiResponse<PageResponseDTO<ReplyLetterSummaryResponseDTO>> getRepliesForLetter(@PathVariable Long letterId,
                                                                                           @Valid PageRequestDTO pageRequestDTO,
                                                                                           BindingResult bindingResult,
                                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        letterBoxService.validateLetterInUserBox(letterId, userId);
        Page<ReplyLetterSummaryResponseDTO> result = letterReplyService.findReplyLetterSummaries(letterId,
                pageRequestDTO, userId);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(summary = "답장 편지 상세 조회", description = "지정된 답장 편지의 ID에 대한 상세 정보를 반환합니다.")
    @GetMapping("/detail/{replyLetterId}")
    public ApiResponse<ReplyLetterDetailResponseDTO> getReplyLetter(@PathVariable Long replyLetterId,
                                                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        letterBoxService.validateLetterInUserBox(replyLetterId, userId);
        return ApiResponse.onSuccess(letterReplyService.findReplyLetterDetail(replyLetterId, userId));
    }

    @Operation(summary = "답장 편지 삭제", description = "답장 편지ID, 송수신 타입(SEND, RECEIVE)을 기반으로 답장 편지를 삭제합니다.")
    @DeleteMapping
    public ApiResponse<String> deleteReplyLetter(
            @RequestBody @Valid ReplyLetterDeleteRequestDTO replyLetterDeleteRequestDTO,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        LetterDeleteDTO letterDeleteDTO = LetterDeleteDTO.fromReplyLetter(replyLetterDeleteRequestDTO);
        letterDeletionService.deleteLetter(letterDeleteDTO, userDetails.getUserId());
        return ApiResponse.onSuccess("답장 편지가 성공적으로 삭제되었습니다.");
    }
}

package postman.bottler.letter.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
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
import postman.bottler.letter.dto.request.PageRequestDTO;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.exception.InvalidPageRequestException;
import postman.bottler.letter.exception.InvalidReplyLetterRequestException;
import postman.bottler.letter.service.DeleteManagerService;
import postman.bottler.letter.service.ReplyLetterService;
import postman.bottler.letter.utiil.ValidationUtil;

@Slf4j
@RestController
@RequestMapping("/letters/replies")
@RequiredArgsConstructor
@Tag(name = "Reply Letters", description = "키워드 편지 API")
public class ReplyLetterController {

    private final ReplyLetterService letterReplyService;
    private final DeleteManagerService deleteManagerService;
    private final ValidationUtil validationUtil;

    @Operation(
            summary = "키워드 편지 생성",
            description = "지정된 편지 ID에 대한 답장을 생성합니다."
    )
    @PostMapping("/{letterId}")
    public ApiResponse<ReplyLetterResponseDTO> createReply(
            @RequestBody @Valid ReplyLetterRequestDTO letterReplyRequestDTO,
            BindingResult bindingResult,
            @PathVariable Long letterId
    ) {
        validateReplyLetterRequest(bindingResult);
        ReplyLetterResponseDTO result = letterReplyService.createReplyLetter(letterId, letterReplyRequestDTO);
        return ApiResponse.onCreateSuccess(result);
    }

    @Operation(
            summary = "특정 키워드 편지에 대한 답장 목록 조회",
            description = "지정된 편지 ID에 대한 답장들의 제목, 라벨이미지, 작성날짜를 페이지네이션 형태로 반환합니다."
    )
    @GetMapping("/{letterId}")
    public ApiResponse<PageResponseDTO<ReplyLetterHeadersResponseDTO>> getReplyForLetter(
            @PathVariable Long letterId,
            @Valid PageRequestDTO pageRequestDTO,
            BindingResult bindingResult
    ) {
        validatePageRequest(bindingResult);
        Page<ReplyLetterHeadersResponseDTO> result =
                letterReplyService.getReplyLetterHeadersById(letterId, pageRequestDTO);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @Operation(
            summary = "답장 편지 상세 조회",
            description = "지정된 답장 편지의 ID에 대한 상세 정보를 반환합니다."
    )
    @GetMapping("/detail/{replyLetterId}")
    public ApiResponse<ReplyLetterResponseDTO> getReplyLetter(
            @PathVariable Long replyLetterId
    ) {
        ReplyLetterResponseDTO result = letterReplyService.getReplyLetterDetail(replyLetterId);
        return ApiResponse.onSuccess(result);
    }

    @Operation(
            summary = "답장 편지 삭제",
            description = "답장 편지ID, 편지타입(LETTER, REPLY_LETTER, 송수신 타입(SEND, RECEIVE)을 기반으로 답장 편지를 삭제합니다."
    )
    @DeleteMapping
    public ApiResponse<String> deleteReplyLetter(
            @RequestBody @Valid LetterDeleteRequestDTO letterDeleteRequestDTO
    ) {
        deleteManagerService.deleteLetters(List.of(letterDeleteRequestDTO));
        return ApiResponse.onSuccess("success");
    }

    private void validateReplyLetterRequest(BindingResult bindingResult) {
        validationUtil.validate(bindingResult,
                errors -> new InvalidReplyLetterRequestException("유효성 검사 실패", errors));
    }

    private void validatePageRequest(BindingResult bindingResult) {
        validationUtil.validate(bindingResult,
                errors -> new InvalidPageRequestException("유효성 검사 실패", errors));
    }
}

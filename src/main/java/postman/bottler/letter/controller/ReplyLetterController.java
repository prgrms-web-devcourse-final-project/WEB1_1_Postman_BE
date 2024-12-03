package postman.bottler.letter.controller;

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
public class ReplyLetterController {

    private final ReplyLetterService letterReplyService;
    private final DeleteManagerService deleteManagerService;
    private final ValidationUtil validationUtil;

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

    @GetMapping("/detail/{replyLetterId}")
    public ApiResponse<ReplyLetterResponseDTO> getReplyLetter(
            @PathVariable Long replyLetterId
    ) {
        ReplyLetterResponseDTO result = letterReplyService.getReplyLetterDetail(replyLetterId);
        return ApiResponse.onSuccess(result);
    }

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

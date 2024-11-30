package postman.bottler.letter.controller;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.exception.InvalidReplyLetterRequestException;
import postman.bottler.letter.service.DeleteManagerService;
import postman.bottler.letter.service.ReplyLetterService;

@Slf4j
@RestController
@RequestMapping("/letters/replies")
@RequiredArgsConstructor
public class ReplyLetterController {

    private final ReplyLetterService letterReplyService;
    private final DeleteManagerService deleteManagerService;

    @PostMapping("/{letterId}")
    public ApiResponse<ReplyLetterResponseDTO> createReply(
            @RequestBody @Valid ReplyLetterRequestDTO letterReplyRequestDTO,
            BindingResult bindingResult,
            @PathVariable Long letterId
    ) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = bindingResult.getFieldErrors().stream()
                    .collect(Collectors.toMap(
                            FieldError::getField,
                            fieldError -> Objects.requireNonNullElse(fieldError.getDefaultMessage(), "검증 실패")
                    ));
            throw new InvalidReplyLetterRequestException("유효성 검사 실패", errors);
        }

        ReplyLetterResponseDTO result = letterReplyService.createReplyLetter(letterId, letterReplyRequestDTO);
        return ApiResponse.onCreateSuccess(result);
    }

    @GetMapping
    public ApiResponse<PageResponseDTO<ReplyLetterHeadersResponseDTO>> getReplyLetterHeaders(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<ReplyLetterHeadersResponseDTO> result = letterReplyService.getReplyLetterHeaders(page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/{letterId}")
    public ApiResponse<PageResponseDTO<ReplyLetterHeadersResponseDTO>> getReplyForLetter(
            @PathVariable Long letterId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        Page<ReplyLetterHeadersResponseDTO> result =
                letterReplyService.getReplyLetterHeadersById(letterId, page, size, sort);
        return ApiResponse.onSuccess(PageResponseDTO.from(result));
    }

    @GetMapping("/detail/{replyLetterId}")
    public ApiResponse<ReplyLetterResponseDTO> getReplyLetter(
            @PathVariable Long replyLetterId
    ) {
        ReplyLetterResponseDTO result = letterReplyService.getReplyLetterDetail(replyLetterId);
        return ApiResponse.onSuccess(result);
    }

    @DeleteMapping("/{replyLetterId}")
    public ApiResponse<String> deleteReplyLetter(@PathVariable Long replyLetterId) {
        deleteManagerService.deleteReplyLetter(replyLetterId);
        return ApiResponse.onSuccess("success");
    }
}

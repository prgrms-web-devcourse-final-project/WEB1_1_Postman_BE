package postman.bottler.letter.controller;

import jakarta.validation.Valid;
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
import postman.bottler.letter.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.dto.response.PageResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.service.ReplyLetterService;

@RestController
@RequestMapping("/letters/replies")
@RequiredArgsConstructor
public class ReplyLetterController {

    private final ReplyLetterService letterReplyService;

    @PostMapping("/{letterId}")
    public ApiResponse<ReplyLetterResponseDTO> createReply(
            @PathVariable Long letterId,
            @RequestBody @Valid ReplyLetterRequestDTO letterReplyRequestDTO
    ) {
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
        letterReplyService.deleteReplyLetter(replyLetterId);
        return ApiResponse.onSuccess("success");
    }
}
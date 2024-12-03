package postman.bottler.complaint.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.complaint.dto.request.ComplaintRequestDTO;
import postman.bottler.complaint.dto.response.ComplaintResponseDTO;
import postman.bottler.complaint.exception.InvalidComplainException;
import postman.bottler.complaint.service.ComplaintService;
import postman.bottler.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
@Tag(name = "신고 API", description = "로그인 사용자만 가능")
public class ComplaintController {
    private final ComplaintService complaintService;

    @Operation(summary = "키워드 편지 신고", description = "신고하는 편지 ID와 신고 사유를 등록합니다.")
    @PostMapping("/letters/{letterId}/complaint")
    public ApiResponse<?> complainKeywordLetter(@PathVariable Long letterId,
                                                @RequestBody ComplaintRequestDTO complaintRequest,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainKeywordLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @Operation(summary = "지도 편지 신고", description = "신고하는 편지 ID와 신고 사유를 등록합니다.")
    @PostMapping("/map/{letterId}/complaint")
    public ApiResponse<?> complainMapLetter(@PathVariable Long letterId,
                                            @RequestBody ComplaintRequestDTO complaintRequest,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainMapLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @Operation(summary = "지도 답장 편지 신고", description = "신고하는 편지 ID와 신고 사유를 등록합니다.")
    @PostMapping("/map/reply/{replyLetterId}/complaint")
    public ApiResponse<?> complainMapReplyLetter(@PathVariable Long replyLetterId,
                                                 @RequestBody ComplaintRequestDTO complaintRequest,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainMapReplyLetter(replyLetterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @Operation(summary = "키워드 답장 편지 신고", description = "신고하는 편지 ID와 신고 사유를 등록합니다.")
    @PostMapping("/letters/reply/{replyLetterId}/complaint")
    public ApiResponse<?> complainKeywordReplyLetter(@PathVariable Long replyLetterId,
                                                     @RequestBody ComplaintRequestDTO complaintRequest,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainKeywordReplyLetter(replyLetterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }


}

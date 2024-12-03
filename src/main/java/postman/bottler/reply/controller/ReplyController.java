package postman.bottler.reply.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.reply.dto.response.ReplyResponseDTO;
import postman.bottler.reply.service.ReplyService;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
@Tag(name="최근 답장 3개 보여주는 컨트롤러")
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping
    public ApiResponse<List<ReplyResponseDTO>> findRecentReplyLetters(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return ApiResponse.onSuccess(replyService.findRecentReplyLetters(customUserDetails.getUserId()));
    }
}

package postman.bottler.reply.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.reply.service.ReplyService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reply")
@Tag(name="최근 답장 3개 보여주는 컨트롤러")
public class ReplyController {
    private final ReplyService replyService;


}

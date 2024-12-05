package postman.bottler.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SlackTestController {

    private final SlackService slackService;

    @GetMapping("/slack")
    public void test(){
        slackService.sendSlackMessage("슬랙 테스트");
    }
}

package postman.bottler.slack;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class SlackTestController {

    private final UserService userService;

    @GetMapping("/slack")
    public void test(){
        userService.updateWarningCount(6L);
    }
}

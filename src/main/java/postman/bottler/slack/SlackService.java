package postman.bottler.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SlackService {
    @Value("${slack.token}")
    String slackToken;

    private static final String CHANNEL_NAME = "#보틀러";

    public void sendSlackMessage(SlackConstant slackConstant, Long userId) {
        String message = String.format(slackConstant.getMessage(), userId);

        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(CHANNEL_NAME)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);

            log.info("Slack " + CHANNEL_NAME + " 에 메세지 보냄");
        } catch (SlackApiException | IOException e) {
            log.error(e.getMessage());
        }
    }
}

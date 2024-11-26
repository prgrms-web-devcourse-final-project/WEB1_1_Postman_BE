package postman.bottler.notification.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PushMessage {
    private String token;

    private String title;

    private String content;
}

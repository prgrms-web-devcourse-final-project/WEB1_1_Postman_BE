package postman.bottler.notification.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushMessages {
    List<PushMessage> messages;

    public static PushMessages from(List<PushMessage> messages) {
        return PushMessages.builder()
                .messages(messages)
                .build();
    }
}

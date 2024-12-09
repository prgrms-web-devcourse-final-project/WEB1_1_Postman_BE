package postman.bottler.notification.domain;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PushMessages {
    private List<PushMessage> messages;

    public static PushMessages from(List<PushMessage> messages) {
        return PushMessages.builder()
                .messages(messages)
                .build();
    }

    public void add(PushMessages messages) {
        this.messages.addAll(messages.getMessages());
    }
}

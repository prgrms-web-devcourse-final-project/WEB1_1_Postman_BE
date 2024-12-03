package postman.bottler.reply.dto.response;

import postman.bottler.reply.dto.ReplyType;

public record ReplyResponseDTO(
        ReplyType type,
        String labelUrl,
        Long letterId
) {
}

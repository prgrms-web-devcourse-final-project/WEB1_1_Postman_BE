package postman.bottler.reply.application.dto.response;

import postman.bottler.reply.application.dto.ReplyType;

public record ReplyResponseDTO(
        ReplyType type,
        String labelUrl,
        Long letterId
) {
    public static ReplyResponseDTO from(ReplyType replyType, String labelUrl, Long letterId) {
        return new ReplyResponseDTO(replyType, labelUrl, letterId);
    }
}

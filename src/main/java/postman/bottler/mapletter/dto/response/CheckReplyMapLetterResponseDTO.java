package postman.bottler.mapletter.dto.response;

public record CheckReplyMapLetterResponseDTO(
        boolean isReplied //true면 답장을 이미 보낸 상태, false면 답장을 보내지 않은 상태
) {
}

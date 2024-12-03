package postman.bottler.reply.dto;

public record LetterLogDTO(
        String name,
        String labelUrl
) {
    public static LetterLogDTO from(String name, String labelUrl) {
        return new LetterLogDTO(name, labelUrl);
    }
}

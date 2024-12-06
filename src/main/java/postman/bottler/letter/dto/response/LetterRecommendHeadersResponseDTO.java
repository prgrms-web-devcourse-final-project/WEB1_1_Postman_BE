package postman.bottler.letter.dto.response;

import postman.bottler.letter.domain.Letter;

public record LetterRecommendHeadersResponseDTO(
        Long letterId,
        String title,
        String label
) {
    public static LetterRecommendHeadersResponseDTO from(Letter letter) {
        return new LetterRecommendHeadersResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getLabel()
        );
    }
}

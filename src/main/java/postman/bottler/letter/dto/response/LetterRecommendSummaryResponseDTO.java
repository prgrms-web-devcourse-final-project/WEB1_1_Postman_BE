package postman.bottler.letter.dto.response;

import postman.bottler.letter.domain.Letter;

public record LetterRecommendSummaryResponseDTO(
        Long letterId,
        String title,
        String label
) {
    public static LetterRecommendSummaryResponseDTO from(Letter letter) {
        return new LetterRecommendSummaryResponseDTO(
                letter.getId(),
                letter.getTitle(),
                letter.getLabel()
        );
    }
}

package postman.bottler.keyword.application.repository;

import java.util.List;
import postman.bottler.letter.domain.RecommendedLetter;

public interface RecommendedLetterRepository {
    void saveRecommendedLetter(RecommendedLetter recommendedLetter);

    List<Long> findRecommendedLettersByUserId(Long userId);
}

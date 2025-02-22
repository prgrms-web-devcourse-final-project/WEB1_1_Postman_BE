package postman.bottler.letter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.application.repository.RecommendedLetterRepository;
import postman.bottler.letter.domain.RecommendedLetter;
import postman.bottler.letter.infra.entity.RecommendedLetterEntity;

@Repository
@RequiredArgsConstructor
public class RecommendedLetterRepositoryImpl implements RecommendedLetterRepository {

    private final RecommendedLetterJpaRepository recommendedLetterJpaRepository;

    @Override
    public void saveRecommendedLetter(RecommendedLetter recommendedLetter) {
        recommendedLetterJpaRepository.save(RecommendedLetterEntity.from(recommendedLetter));
    }

    @Override
    public List<Long> findRecommendedLettersByUserId(Long userId) {
        return recommendedLetterJpaRepository.findRecommendedLetterIdsByUserId(userId);
    }
}

package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.application.dto.RecommendedLetterDTO;
import postman.bottler.keyword.application.repository.RecommendedLetterRepository;

@Service
@RequiredArgsConstructor
public class RecommendedLetterService {

    private final RecommendedLetterRepository recommendedLetterRepository;

    public void saveRecommendedLetter(RecommendedLetterDTO recommendedLetterDTO) {
        recommendedLetterRepository.saveRecommendedLetter(recommendedLetterDTO.toDomain());
    }

    public List<Long> findRecommendedLetterIdsByUserId(Long userId) {
        return recommendedLetterRepository.findRecommendedLettersByUserId(userId);
    }
}

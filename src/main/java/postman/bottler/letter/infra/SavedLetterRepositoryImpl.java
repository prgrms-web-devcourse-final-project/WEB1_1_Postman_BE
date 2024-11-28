package postman.bottler.letter.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.SavedLetter;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.infra.entity.SavedLetterEntity;
import postman.bottler.letter.service.SavedLetterRepository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class SavedLetterRepositoryImpl implements SavedLetterRepository {

    private final SavedLetterJpaRepository savedLetterJpaRepository;

    @Override
    public void save(SavedLetter savedLetter) {
        savedLetterJpaRepository.save(SavedLetterEntity.from(savedLetter));
    }

    @Override
    public boolean isSaved(Long userId, Long letterId) {
        return savedLetterJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public void remove(Long userId, Long letterId) {
        savedLetterJpaRepository.deleteByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public Page<Letter> findSavedLetters(Long userId, Pageable pageable) {
        return savedLetterJpaRepository.findSavedLettersByUserId(userId, pageable)
                .map(LetterEntity::toDomain);
    }
}

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
public class SavedRepositoryJpaImpl implements SavedLetterRepository {

    private final SavedJpaRepository savedJpaRepository;

    @Override
    public void save(SavedLetter savedLetter) {
        savedJpaRepository.save(SavedLetterEntity.from(savedLetter));
    }

    @Override
    public boolean isAlreadySaved(Long userId, Long letterId) {
        return savedJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public void remove(Long userId, Long letterId) {
        savedJpaRepository.deleteByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public boolean existsById(Long userId, Long letterId) {
        return savedJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public Page<Letter> findSavedLetters(Long userId, Pageable pageable) {
        return savedJpaRepository.findSavedLettersByUserId(userId, pageable)
                .map(LetterEntity::toDomain);
    }
}

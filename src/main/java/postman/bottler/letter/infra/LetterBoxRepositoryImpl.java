package postman.bottler.letter.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.infra.entity.LetterBoxEntity;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.service.LetterBoxRepository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LetterBoxRepositoryImpl implements LetterBoxRepository {

    private final LetterBoxJpaRepository letterBoxJpaRepository;

    @Override
    public void save(LetterBox letterBox) {
        letterBoxJpaRepository.save(LetterBoxEntity.from(letterBox));
    }

    @Override
    public boolean isSaved(Long userId, Long letterId) {
        return letterBoxJpaRepository.existsByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public void remove(Long userId, Long letterId) {
        letterBoxJpaRepository.deleteByUserIdAndLetterId(userId, letterId);
    }

    @Override
    public Page<Letter> findSavedLetters(Long userId, Pageable pageable) {
        return letterBoxJpaRepository.findSavedLettersByUserId(userId, pageable)
                .map(LetterEntity::toDomain);
    }
}

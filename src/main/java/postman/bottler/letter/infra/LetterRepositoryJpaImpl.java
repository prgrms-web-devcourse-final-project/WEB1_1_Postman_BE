package postman.bottler.letter.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.service.LetterRepository;

@Repository
@RequiredArgsConstructor
public class LetterRepositoryJpaImpl implements LetterRepository {

    private final LetterJpaRepository letterJpaRepository;

    @Override
    @Transactional
    public Letter save(Letter letter) {
        LetterEntity letterEntity = letterJpaRepository.save(LetterEntity.from(letter));
        return letterEntity.toDomain();
    }

    @Override
    public void remove(Long letterId) {
        letterJpaRepository.deleteById(letterId);
    }

    @Override
    public Optional<Letter> findById(Long letterId) {
        return letterJpaRepository.findById(letterId)
                .map(LetterEntity::toDomain);
    }
}

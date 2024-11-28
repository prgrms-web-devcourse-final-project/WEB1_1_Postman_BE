package postman.bottler.letter.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.service.LetterRepository;

@Repository
@RequiredArgsConstructor
public class LetterRepositoryImpl implements LetterRepository {

    private final LetterJpaRepository letterJpaRepository;

    @Override
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

    @Override
    public boolean existsById(Long letterId) {
        return letterJpaRepository.existsById(letterId);
    }

    @Override
    public Page<Letter> findAll(Long userId, Pageable pageable) {
        return letterJpaRepository.findAllByUserId(userId, pageable)
                .map(LetterEntity::toDomain);
    }
}

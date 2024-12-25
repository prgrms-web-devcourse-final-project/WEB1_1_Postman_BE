package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.application.repository.LetterRepository;

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
    public Optional<Letter> findById(Long letterId) {
        return letterJpaRepository.findById(letterId)
                .map(LetterEntity::toDomain);
    }

    @Override
    public List<Letter> findAllActiveByIds(List<Long> letterIds) {
        return letterJpaRepository.findAllByIds(letterIds).stream()
                .map(LetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<Letter> findAllByUserId(Long userId) {
        return letterJpaRepository.findAllByUserId(userId).stream()
                .map(LetterEntity::toDomain)
                .toList();
    }

    @Override
    public void softDeleteByIds(List<Long> letterIds) {
        letterJpaRepository.softDeleteByIds(letterIds);
    }

    @Override
    public void softBlockById(Long letterId) {
        letterJpaRepository.softBlockById(letterId);
    }

    @Override
    public boolean existsById(Long letterId) {
        return letterJpaRepository.existsById(letterId);
    }
}

package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public Optional<Letter> findById(Long letterId) {
        return letterJpaRepository.findActiveById(letterId)
                .map(LetterEntity::toDomain);
    }

    @Override
    public void deleteByIds(List<Long> letterIds) {
        letterJpaRepository.deleteByIds(letterIds);
    }

    @Override
    public void blockLetterById(Long letterId) {
        letterJpaRepository.blockById(letterId);
    }

    @Override
    public List<Letter> findAllByIds(List<Long> letterIds) {
        return letterJpaRepository.findAllByIds(letterIds).stream()
                .map(LetterEntity::toDomain)
                .toList();
    }

    @Override
    public boolean checkLetterExists(Long letterId) {
        return letterJpaRepository.existsById(letterId);
    }
}

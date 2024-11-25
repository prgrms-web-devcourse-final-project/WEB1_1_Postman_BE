package postman.bottler.letter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.service.LetterRepository;

@Repository
@RequiredArgsConstructor
public class LetterRepositoryJpaImpl implements LetterRepository {

    private final LetterJpaRepository letterJpaRepository;

    @Override
    public Letter save(Letter letter) {
        LetterEntity letterEntity = letterJpaRepository.save(LetterEntity.from(letter));
        return letterEntity.toDomain();
    }
}

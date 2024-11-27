package postman.bottler.letter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;
import postman.bottler.letter.service.ReplyLetterRepository;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRepositoryImpl implements ReplyLetterRepository {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public ReplyLetter save(ReplyLetter replyLetter) {
        ReplyLetterEntity replyLetterEntity = replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter));
        return replyLetterEntity.toDomain();
    }
}

package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;
import postman.bottler.mapletter.service.ReplyMapLetterRepository;

@Repository
@RequiredArgsConstructor
public class ReplyMapLetterRepositoryImpl implements ReplyMapLetterRepository {
    private final ReplyMapLetterJpaRepository replyMapLetterJpaRepository;

    @Override
    @Transactional
    public ReplyMapLetter save(ReplyMapLetter replyMapLetter) {
        ReplyMapLetterEntity replyMapLetterEntity = ReplyMapLetterEntity.from(replyMapLetter);
        ReplyMapLetterEntity save = replyMapLetterJpaRepository.save(replyMapLetterEntity);
        return ReplyMapLetterEntity.toDomain(save);
    }

}

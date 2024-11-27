package postman.bottler.letter.infra;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;
import postman.bottler.letter.service.ReplyLetterRepository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ReplyLetterRepositoryImpl implements ReplyLetterRepository {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public ReplyLetter save(ReplyLetter replyLetter) {
        ReplyLetterEntity replyLetterEntity = replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter));
        return replyLetterEntity.toDomain();
    }

    @Override
    public Page<ReplyLetter> findAll(Long userId, Pageable pageable) {
        Page<ReplyLetter> map = replyLetterJpaRepository.findAllByUserId(userId, pageable)
                .map(ReplyLetterEntity::toDomain);

        log.error(String.valueOf(map.getContent().get(0).getId()));
        return replyLetterJpaRepository.findAllByUserId(userId, pageable)
                .map(ReplyLetterEntity::toDomain);
    }
}

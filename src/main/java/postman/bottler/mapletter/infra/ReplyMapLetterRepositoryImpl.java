package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;
import postman.bottler.mapletter.service.ReplyMapLetterRepository;

import java.util.List;

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

    @Override
    public List<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId) {
        List<ReplyMapLetterEntity> findActiveLetters=replyMapLetterJpaRepository.findActiveReplyMapLettersBySourceUserId(userId);

        return findActiveLetters.stream()
                .map(ReplyMapLetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId) {
        List<ReplyMapLetterEntity> findActiveLetters=replyMapLetterJpaRepository.findReplyMapLettersBySourceLetterId(letterId);

        return findActiveLetters.stream()
                .map(ReplyMapLetterEntity::toDomain)
                .toList();
    }
}

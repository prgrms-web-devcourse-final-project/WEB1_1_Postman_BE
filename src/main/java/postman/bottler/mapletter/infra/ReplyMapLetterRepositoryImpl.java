package postman.bottler.mapletter.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;
import postman.bottler.mapletter.service.ReplyMapLetterRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReplyMapLetterRepositoryImpl implements ReplyMapLetterRepository {
    private final ReplyMapLetterJpaRepository replyMapLetterJpaRepository;
    private final EntityManager em;

    @Override
    @Transactional
    public ReplyMapLetter save(ReplyMapLetter replyMapLetter) {
        ReplyMapLetterEntity replyMapLetterEntity = ReplyMapLetterEntity.from(replyMapLetter);
        ReplyMapLetterEntity save = replyMapLetterJpaRepository.save(replyMapLetterEntity);
        return ReplyMapLetterEntity.toDomain(save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId) {
        List<ReplyMapLetterEntity> findActiveLetters=replyMapLetterJpaRepository.findActiveReplyMapLettersBySourceUserId(userId);

        return findActiveLetters.stream()
                .map(ReplyMapLetterEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId) {
        List<ReplyMapLetterEntity> findActiveLetters=replyMapLetterJpaRepository.findReplyMapLettersBySourceLetterId(letterId);

        return findActiveLetters.stream()
                .map(ReplyMapLetterEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyMapLetter findById(Long letterId) {
        ReplyMapLetterEntity replyMapLetterEntity = replyMapLetterJpaRepository.findById(letterId)
                .orElseThrow(()->new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        return ReplyMapLetterEntity.toDomain(replyMapLetterEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean findByLetterIdAndUserId(Long letterId, Long userId) {
        return replyMapLetterJpaRepository.findBySourceLetterIdAndCreateUserId(letterId, userId).isPresent();
        // 값이 없으면 false
    }

    @Override
    @Transactional
    public void letterBlock(Long letterId) {
        replyMapLetterJpaRepository.letterBlock(letterId);
    }

    @Override
    @Transactional
    public void softDelete(Long letterId) {
        ReplyMapLetterEntity letter = replyMapLetterJpaRepository.findById(letterId)
                .orElseThrow(()->new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        ReplyMapLetterEntity replyMapLetter = em.find(ReplyMapLetterEntity.class, letterId);
        replyMapLetter.updateDelete(true);
    }
}

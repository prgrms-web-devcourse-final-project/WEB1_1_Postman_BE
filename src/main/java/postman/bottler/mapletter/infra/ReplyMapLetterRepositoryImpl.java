package postman.bottler.mapletter.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;
import postman.bottler.mapletter.service.ReplyMapLetterRepository;

@Repository
@RequiredArgsConstructor
public class ReplyMapLetterRepositoryImpl implements ReplyMapLetterRepository {
    private final ReplyMapLetterJpaRepository replyMapLetterJpaRepository;
    private final EntityManager em;

    @Override
    public ReplyMapLetter save(ReplyMapLetter replyMapLetter) {
        ReplyMapLetterEntity replyMapLetterEntity = ReplyMapLetterEntity.from(replyMapLetter);
        ReplyMapLetterEntity save = replyMapLetterJpaRepository.save(replyMapLetterEntity);
        return ReplyMapLetterEntity.toDomain(save);
    }

    @Override
    public Page<ReplyMapLetter> findActiveReplyMapLettersBySourceUserId(Long userId, Pageable pageable) {
        Page<ReplyMapLetterEntity> findActiveLetters = replyMapLetterJpaRepository.findActiveReplyMapLettersBySourceUserId(
                userId, pageable);

        return findActiveLetters.map(ReplyMapLetterEntity::toDomain);
    }

    @Override
    public Page<ReplyMapLetter> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable) {
        Page<ReplyMapLetterEntity> findActiveLetters = replyMapLetterJpaRepository.findReplyMapLettersBySourceLetterId(
                letterId, pageable);

        return findActiveLetters.map(ReplyMapLetterEntity::toDomain);
    }

    @Override
    public ReplyMapLetter findById(Long letterId) {
        ReplyMapLetterEntity replyMapLetterEntity = replyMapLetterJpaRepository.findById(letterId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        return ReplyMapLetterEntity.toDomain(replyMapLetterEntity);
    }

    @Override
    public boolean findByLetterIdAndUserId(Long letterId, Long userId) {
        return replyMapLetterJpaRepository.findBySourceLetterIdAndCreateUserId(letterId, userId).isPresent();
        // 값이 없으면 false
    }

    @Override
    public void letterBlock(Long letterId) {
        replyMapLetterJpaRepository.letterBlock(letterId);
    }

    @Override
    public void softDelete(Long letterId) {
        ReplyMapLetterEntity letter = replyMapLetterJpaRepository.findById(letterId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        ReplyMapLetterEntity replyMapLetter = em.find(ReplyMapLetterEntity.class, letterId);
        replyMapLetter.updateDelete(true);
    }

    @Override
    public Page<ReplyMapLetter> findAllSentReplyByUserId(Long userId, PageRequest pageRequest) {
        Page<ReplyMapLetterEntity> letters = replyMapLetterJpaRepository.findAllSentReplyByUserId(userId, pageRequest);
        return letters.map(ReplyMapLetterEntity::toDomain);
    }

}

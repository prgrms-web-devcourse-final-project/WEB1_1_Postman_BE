package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.application.repository.ReplyLetterRepository;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

@Repository
@RequiredArgsConstructor
public class ReplyLetterRepositoryImpl implements ReplyLetterRepository {

    private final ReplyLetterJpaRepository replyLetterJpaRepository;

    @Override
    public ReplyLetter save(ReplyLetter replyLetter) {
        ReplyLetterEntity replyLetterEntity = replyLetterJpaRepository.save(ReplyLetterEntity.from(replyLetter));
        return replyLetterEntity.toDomain();
    }

    @Override
    public Page<ReplyLetter> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable) {
        return replyLetterJpaRepository.findAllByLetterIdAndReceiverId(letterId, receiverId, pageable)
                .map(ReplyLetterEntity::toDomain);
    }

    @Override
    public Optional<ReplyLetter> findById(Long replyLetterId) {
        return replyLetterJpaRepository.findById(replyLetterId)
                .map(ReplyLetterEntity::toDomain);
    }

    @Override
    public List<ReplyLetter> findAllByIds(List<Long> letterIds) {
        return replyLetterJpaRepository.findAllByIds(letterIds).stream()
                .map(ReplyLetterEntity::toDomain)
                .toList();
    }

    @Override
    @Transactional
    public void softDeleteByIds(List<Long> letterIds) {
        replyLetterJpaRepository.softDeleteByIds(letterIds);
    }

    @Override
    @Transactional
    public void softBlockById(Long replyLetterId) {
        replyLetterJpaRepository.softBlockById(replyLetterId);
    }

    @Override
    public boolean existsByLetterIdAndSenderId(Long letterId, Long senderId) {
        return replyLetterJpaRepository.existsByLetterIdAndSenderId(letterId, senderId);
    }

    @Override
    public boolean existsByIdAndSenderId(Long replyLetterId, Long userId) {
        return replyLetterJpaRepository.existsByIdAndSenderId(replyLetterId, userId);
    }
}

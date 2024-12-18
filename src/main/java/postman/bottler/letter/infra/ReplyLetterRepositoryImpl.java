package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
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
    public void deleteByIds(List<Long> letterIds) {
        replyLetterJpaRepository.deleteByIds(letterIds);
    }

    @Override
    public void blockReplyLetterById(Long replyLetterId) {
        replyLetterJpaRepository.blockById(replyLetterId);
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

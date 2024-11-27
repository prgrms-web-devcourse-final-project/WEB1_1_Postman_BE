package postman.bottler.letter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {
    Page<ReplyLetterEntity> findAllByReceiverId(Long userId, Pageable pageable);

    Page<ReplyLetterEntity> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);
}

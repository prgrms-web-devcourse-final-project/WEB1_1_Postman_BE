package postman.bottler.letter.infra;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {
    Page<ReplyLetterEntity> findAllByReceiverId(Long userId, Pageable pageable);

    Page<ReplyLetterEntity> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM LetterEntity l WHERE l.id IN :letterIds")
    void deleteByIds(List<Long> letterIds);

    Page<ReplyLetterEntity> findAllByReceiverIdOrderByCreatedAtAsc(Long userId, Pageable pageable);
}

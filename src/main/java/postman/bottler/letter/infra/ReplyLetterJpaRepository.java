package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {

    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.letterId = :letterId AND r.receiverId = :receiverId AND r.isDeleted = false")
    Page<ReplyLetterEntity> findAllByLetterIdAndReceiverId(Long letterId, Long receiverId, Pageable pageable);

    @Override
    @NotNull
    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.id = :id AND r.isDeleted = false")
    Optional<ReplyLetterEntity> findById(@NotNull Long id);

    @Query("SELECT r FROM ReplyLetterEntity r WHERE r.id IN :replyLetterIds AND r.isDeleted = false")
    List<ReplyLetter> findAllByIds(List<Long> replyLetterIds);

    @Modifying
    @Query("UPDATE ReplyLetterEntity r SET r.isDeleted = true WHERE r.id IN :ids")
    void deleteByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE ReplyLetterEntity l SET l.isBlocked = true, l.isDeleted = true WHERE l.id = :id")
    void blockById(Long id);

    boolean existsByLetterIdAndSenderId(Long letterId, Long senderId);

    boolean existsByIdAndSenderId(Long id, Long senderId);
}

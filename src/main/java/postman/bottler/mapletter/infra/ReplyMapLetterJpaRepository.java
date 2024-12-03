package postman.bottler.mapletter.infra;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;

@Repository
public interface ReplyMapLetterJpaRepository extends JpaRepository<ReplyMapLetterEntity, Long> {

    @Query("SELECT r FROM ReplyMapLetterEntity r, MapLetterEntity m WHERE m.createUserId = :userId " +
            "AND m.mapLetterId = r.sourceLetterId AND r.isDeleted=false AND r.isBlocked = false ORDER BY r.createdAt DESC ")
    Page<ReplyMapLetterEntity> findActiveReplyMapLettersBySourceUserId(Long userId, Pageable pageable);

    @Query("SELECT r FROM ReplyMapLetterEntity r " +
            "WHERE r.sourceLetterId=:letterId AND r.isDeleted=false AND r.isBlocked = false")
    Page<ReplyMapLetterEntity> findReplyMapLettersBySourceLetterId(Long letterId, Pageable pageable);

    Optional<ReplyMapLetterEntity> findBySourceLetterIdAndCreateUserId(Long letterId, Long userId);

    @Modifying
    @Query("UPDATE ReplyMapLetterEntity r SET r.isBlocked = true WHERE r.replyLetterId = :letterId")
    void letterBlock(Long letterId);

    @Query("SELECT r FROM ReplyMapLetterEntity r WHERE r.createUserId=:userId AND r.isBlocked=false AND r.isDeleted=false"
            + " ORDER BY r.createdAt DESC")
    Page<ReplyMapLetterEntity> findAllSentReplyByUserId(Long userId, PageRequest pageRequest);

    @Query("SELECT r FROM ReplyMapLetterEntity r, MapLetterEntity m "
            + "WHERE r.sourceLetterId = m.mapLetterId AND m.createUserId=:userId AND r.isDeleted = false AND r.isBlocked = false "
            + "ORDER BY r.createdAt DESC LIMIT :itemsToFetch")
    List<ReplyMapLetterEntity> findRecentReplyByUserId(Long userId, Pageable pageable);
}

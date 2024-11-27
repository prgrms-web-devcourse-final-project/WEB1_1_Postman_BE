package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.infra.entity.ReplyMapLetterEntity;

import java.util.List;

@Repository
public interface ReplyMapLetterJpaRepository extends JpaRepository<ReplyMapLetterEntity, Long> {

    @Query("SELECT r FROM ReplyMapLetterEntity r, MapLetterEntity m WHERE m.createUserId = :userId " +
            "AND m.mapLetterId = r.sourceLetterId AND r.isDeleted=false AND r.isBlocked = false ")
    List<ReplyMapLetterEntity> findActiveReplyMapLettersBySourceUserId(Long userId);

    @Query("SELECT r FROM ReplyMapLetterEntity r " +
            "WHERE r.sourceLetterId=:letterId AND r.isDeleted=false AND r.isBlocked = false")
    List<ReplyMapLetterEntity> findReplyMapLettersBySourceLetterId(Long letterId);
}

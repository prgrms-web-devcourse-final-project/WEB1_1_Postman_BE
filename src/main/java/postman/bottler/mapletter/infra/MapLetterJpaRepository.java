package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

import java.util.List;

@Repository
public interface MapLetterJpaRepository extends JpaRepository<MapLetterEntity, Long> {
    @Query("SELECT m FROM MapLetterEntity m WHERE m.createUserId = :userId AND m.isDeleted = false")
    List<MapLetterEntity> findActiveByCreateUserId(Long userId);

    @Query("SELECT m FROM MapLetterEntity m WHERE m.targetUserId = :userId AND m.isDeleted = false")
    List<MapLetterEntity> findActiveByTargetUserId(Long userId);
}

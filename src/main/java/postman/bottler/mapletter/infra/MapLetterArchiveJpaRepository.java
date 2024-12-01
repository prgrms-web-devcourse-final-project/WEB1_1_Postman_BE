package postman.bottler.mapletter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;

import java.util.Optional;

@Repository
public interface MapLetterArchiveJpaRepository extends JpaRepository<MapLetterArchiveEntity, Long> {

    @Query("SELECT new postman.bottler.mapletter.dto.response.FindAllArchiveLetters("
            + "a.mapLetterArchiveId, m.mapLetterId, m.title, m.description, m.latitude, m.longitude, m.label, "
            + "a.createdAt, m.createdAt) "
            + "FROM MapLetterArchiveEntity a, MapLetterEntity m "
            + "WHERE a.mapLetterId = m.mapLetterId AND m.isBlocked = false AND m.isDeleted=false AND a.userId=:userId"
            + " ORDER BY a.createdAt DESC")
    Page<FindAllArchiveLetters> findAllByUserId(Long userId, Pageable pageable);

    Optional<MapLetterArchiveEntity> findByMapLetterIdAndUserId(Long letterId, Long userId);
}

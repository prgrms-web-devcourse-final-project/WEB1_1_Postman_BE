package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface MapLetterArchiveJpaRepository extends JpaRepository<MapLetterArchiveEntity, Long> {

    @Transactional
    @Query("SELECT new postman.bottler.mapletter.dto.response.FindAllArchiveLetters(" +
            "a.mapLetterArchiveId, m.mapLetterId, m.title, m.description, m.label, m.createdAt)" +
            "FROM MapLetterArchiveEntity a, MapLetterEntity m " +
            "WHERE a.mapLetterId = m.mapLetterId AND m.isBlocked = false AND m.isDeleted=false AND a.userId=:userId")
    List<FindAllArchiveLetters> findAllByUserId(Long userId);

    Optional<MapLetterArchiveEntity> findByMapLetterIdAndUserId(Long letterId, Long userId);
}

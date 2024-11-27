package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;

import java.util.List;

@Repository
public interface MapLetterArchiveJpaRepository extends JpaRepository<MapLetterArchiveEntity, Long> {
    @Query("SELECT new postman.bottler.mapletter.dto.response.FindAllArchiveLetters(" +
            "m.mapLetterId, m.title, m.description, m.label, m.createdAt)" +
            "FROM MapLetterArchiveEntity a, MapLetterEntity m " +
            "WHERE a.mapLetterId = m.mapLetterId AND m.isBlocked = false AND m.isDeleted=false")
    List<FindAllArchiveLetters> findAllByUserId(Long userId);
}

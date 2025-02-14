package postman.bottler.label.infra;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.label.domain.LabelType;
import postman.bottler.label.infra.entity.LabelEntity;

public interface LabelJpaRepository extends JpaRepository<LabelEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT l FROM LabelEntity l WHERE l.labelId = :labelId")
    Optional<LabelEntity> findByIdWithLock(@Param("labelId") Long labelId);

    List<LabelEntity> findByLabelType(LabelType labelType);
}

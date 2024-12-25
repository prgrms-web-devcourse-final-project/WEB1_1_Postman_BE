package postman.bottler.complaint.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.complaint.infra.entity.KeywordComplaintEntity;

public interface KeywordComplaintJpaRepository extends JpaRepository<KeywordComplaintEntity, Long> {
    List<KeywordComplaintEntity> findByLetterId(Long letterId);
}

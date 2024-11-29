package postman.bottler.complaint.infra.keyword;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKeywordComplaintRepository extends JpaRepository<KeywordComplaintEntity, Long> {
    List<KeywordComplaintEntity> findByLetterId(Long letterId);
}

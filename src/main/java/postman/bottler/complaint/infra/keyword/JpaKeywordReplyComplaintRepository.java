package postman.bottler.complaint.infra.keyword;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaKeywordReplyComplaintRepository extends JpaRepository<KeywordReplyComplaintEntity, Long> {
    List<KeywordReplyComplaintEntity> findByLetterId(Long letterId);
}

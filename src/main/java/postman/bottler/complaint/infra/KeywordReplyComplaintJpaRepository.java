package postman.bottler.complaint.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.complaint.infra.entity.KeywordReplyComplaintEntity;

public interface KeywordReplyComplaintJpaRepository extends JpaRepository<KeywordReplyComplaintEntity, Long> {
    List<KeywordReplyComplaintEntity> findByLetterId(Long letterId);
}

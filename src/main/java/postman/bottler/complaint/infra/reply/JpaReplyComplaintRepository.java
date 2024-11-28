package postman.bottler.complaint.infra.reply;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReplyComplaintRepository extends JpaRepository<ReplyComplaintEntity, Long> {
    List<ReplyComplaintEntity> findByLetterId(Long letterId);
}

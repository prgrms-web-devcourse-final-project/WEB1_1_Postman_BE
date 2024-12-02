package postman.bottler.complaint.infra.map;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMapReplyComplaintRepository extends JpaRepository<MapReplyComplaintEntity, Long> {
    List<MapReplyComplaintEntity> findByLetterId(Long letterId);
}

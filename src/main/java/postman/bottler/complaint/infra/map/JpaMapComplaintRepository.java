package postman.bottler.complaint.infra.map;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaMapComplaintRepository extends JpaRepository<MapComplaintEntity, Long> {
    List<MapComplaintEntity> findByLetterId(Long letterId);
}

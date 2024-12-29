package postman.bottler.complaint.infra;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.application.repository.MapComplaintRepository;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.infra.entity.MapComplaintEntity;

@Repository
@RequiredArgsConstructor
public class MapComplaintRepositoryImpl implements MapComplaintRepository {
    private final MapComplaintJpaRepository jpaRepository;

    @Override
    public Complaint save(Complaint complaint) {
        return jpaRepository.save(MapComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<MapComplaintEntity> entities = jpaRepository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(MapComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

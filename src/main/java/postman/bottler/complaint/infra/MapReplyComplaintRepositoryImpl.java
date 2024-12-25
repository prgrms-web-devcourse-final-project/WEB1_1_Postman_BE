package postman.bottler.complaint.infra;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.application.repository.MapReplyComplaintRepository;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.infra.entity.MapReplyComplaintEntity;

@Repository
@RequiredArgsConstructor
public class MapReplyComplaintRepositoryImpl implements MapReplyComplaintRepository {
    private final MapReplyComplaintJpaRepository repository;

    @Override
    public Complaint save(Complaint complaint) {
        return repository.save(MapReplyComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<MapReplyComplaintEntity> entities = repository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(MapReplyComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

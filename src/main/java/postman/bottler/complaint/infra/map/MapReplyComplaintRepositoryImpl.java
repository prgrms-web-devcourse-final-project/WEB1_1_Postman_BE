package postman.bottler.complaint.infra.map;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.MapReplyComplaint;
import postman.bottler.complaint.service.MapReplyComplaintRepository;

@Repository
@RequiredArgsConstructor
public class MapReplyComplaintRepositoryImpl implements MapReplyComplaintRepository {
    private final JpaMapReplyComplaintRepository repository;

    @Override
    public MapReplyComplaint save(MapReplyComplaint complaint) {
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

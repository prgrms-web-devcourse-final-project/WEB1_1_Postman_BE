package postman.bottler.complaint.infra.reply;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.ReplyComplaint;
import postman.bottler.complaint.service.ReplyComplaintRepository;

@Repository
@RequiredArgsConstructor
public class ReplyComplaintRepositoryImpl implements ReplyComplaintRepository {
    private final JpaReplyComplaintRepository repository;

    @Override
    public ReplyComplaint save(ReplyComplaint complaint) {
        return repository.save(ReplyComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<ReplyComplaintEntity> entities = repository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(ReplyComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

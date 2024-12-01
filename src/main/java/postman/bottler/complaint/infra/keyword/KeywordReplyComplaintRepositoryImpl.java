package postman.bottler.complaint.infra.keyword;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordReplyComplaint;
import postman.bottler.complaint.service.KeywordReplyComplaintRepository;

@Repository
@RequiredArgsConstructor
public class KeywordReplyComplaintRepositoryImpl implements KeywordReplyComplaintRepository {
    private final JpaKeywordReplyComplaintRepository repository;

    @Override
    public KeywordReplyComplaint save(KeywordReplyComplaint complaint) {
        return repository.save(KeywordReplyComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        List<KeywordReplyComplaintEntity> entities = repository.findByLetterId(letterId);
        return Complaints.from(entities.stream()
                .map(KeywordReplyComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

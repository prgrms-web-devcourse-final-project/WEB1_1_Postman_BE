package postman.bottler.complaint.infra.keyword;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.application.repository.KeywordComplaintRepository;

@Repository
@RequiredArgsConstructor
public class KeywordComplaintRepositoryImpl implements KeywordComplaintRepository {
    private final JpaKeywordComplaintRepository repository;

    @Override
    public Complaint save(Complaint complaint) {
        return repository.save(KeywordComplaintEntity.from(complaint)).toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        return Complaints.from(repository.findByLetterId(letterId)
                .stream().map(KeywordComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

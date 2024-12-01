package postman.bottler.complaint.infra.keyword;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordComplaint;
import postman.bottler.complaint.service.KeywordComplaintRepository;

@Repository
@RequiredArgsConstructor
public class KeywordComplaintRepositoryImpl implements KeywordComplaintRepository {
    private final JpaKeywordComplaintRepository repository;

    @Override
    public KeywordComplaint save(KeywordComplaint complaint) {
        KeywordComplaintEntity save = repository.save(KeywordComplaintEntity.from(complaint));
        return save.toDomain();
    }

    @Override
    public Complaints findByLetterId(Long letterId) {
        return Complaints.from(repository.findByLetterId(letterId)
                .stream().map(KeywordComplaintEntity::toDomain)
                .collect(Collectors.toList()));
    }
}

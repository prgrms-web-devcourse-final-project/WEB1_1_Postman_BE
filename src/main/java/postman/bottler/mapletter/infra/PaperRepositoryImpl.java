package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.infra.entity.PaperEntity;
import postman.bottler.mapletter.application.repository.PaperRepository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaperRepositoryImpl implements PaperRepository {
    private final PaperJpaRepository paperJpaRepository;

    @Override
    public List<Paper> findAll() {
        List<PaperEntity> findAllPapers = paperJpaRepository.findAll();

        return findAllPapers.stream()
                .map(PaperEntity::toDomain)
                .toList();
    }
}

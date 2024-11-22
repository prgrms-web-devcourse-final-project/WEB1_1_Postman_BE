package postman.bottler.label.infra;

import org.springframework.stereotype.Repository;
import postman.bottler.label.domain.Label;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.service.LabelRepository;

@Repository
public class LabelRepositoryImpl implements LabelRepository {
    private final LabelJpaRepository labelJpaRepository;

    public LabelRepositoryImpl(LabelJpaRepository labelJpaRepository) {
        this.labelJpaRepository = labelJpaRepository;
    }

    @Override
    public void save(Label label) {
        labelJpaRepository.save(LabelEntity.from(label));
    }
}

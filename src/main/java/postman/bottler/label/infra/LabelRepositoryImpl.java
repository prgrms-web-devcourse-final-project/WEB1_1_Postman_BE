package postman.bottler.label.infra;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import postman.bottler.label.domain.Label;
import postman.bottler.label.exception.InvalidLabelException;
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
        try {
            labelJpaRepository.save(LabelEntity.from(label));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidLabelException("이미 존재하는 라벨입니다.");
        }
    }

    @Override
    public List<Label> findAllLabels() {
        List<LabelEntity> labelEntities = labelJpaRepository.findAll();
        return LabelEntity.toLabels(labelEntities);
    }
}

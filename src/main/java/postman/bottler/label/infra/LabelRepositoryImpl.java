package postman.bottler.label.infra;

import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import postman.bottler.label.domain.Label;
import postman.bottler.label.exception.InvalidLabelException;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.service.LabelRepository;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.entity.UserEntity;

@Repository
public class LabelRepositoryImpl implements LabelRepository {
    private final LabelJpaRepository labelJpaRepository;
    private final UserLabelJpaRepository userLabelJpaRepository;

    public LabelRepositoryImpl(LabelJpaRepository labelJpaRepository, UserLabelJpaRepository userLabelJpaRepository) {
        this.labelJpaRepository = labelJpaRepository;
        this.userLabelJpaRepository = userLabelJpaRepository;
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

    @Override
    public List<Label> findLabelsByUser(User user) {
        List<LabelEntity> labelEntities = userLabelJpaRepository.findLabelsByUser(UserEntity.from(user));
        return LabelEntity.toLabels(labelEntities);
    }
}

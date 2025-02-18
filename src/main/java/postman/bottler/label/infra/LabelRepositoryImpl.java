package postman.bottler.label.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.label.domain.Label;
import postman.bottler.label.domain.LabelType;
import postman.bottler.label.exception.InvalidLabelException;
import postman.bottler.label.exception.UserLabelNotFoundException;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.infra.entity.UserLabelEntity;
import postman.bottler.label.application.repository.LabelRepository;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.UserJpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

@Repository
@RequiredArgsConstructor
public class LabelRepositoryImpl implements LabelRepository {

    private final LabelJpaRepository labelJpaRepository;
    private final UserLabelJpaRepository userLabelJpaRepository;
    private final UserJpaRepository userJpaRepository;

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
    public List<Label> findLabelsByUser(Long userId) {
        List<LabelEntity> labelEntities = userLabelJpaRepository.findLabelsByUserId(userId);

        if (labelEntities.isEmpty()) {
            throw new UserLabelNotFoundException("유저 ID " + userId + " 에 해당하는 라벨이 존재하지 않습니다.");
        }

        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    @Transactional
    public Label findLabelByLabelId(Long labelId) {
        LabelEntity labelEntity = labelJpaRepository.findByIdWithLock(labelId)
                .orElseThrow(() -> new InvalidLabelException("라벨 ID " + labelId + " 에 해당하는 라벨이 존재하지 않습니다."));
        return LabelEntity.toLabel(labelEntity);
    }

    @Override
    public void updateOwnedCount(Label label) {
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
        labelEntity.updateOwnedCount();
    }

    @Override
    public void createUserLabel(User user, Label label) {
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
        UserEntity userEntity = userJpaRepository.findById(user.getUserId())
                .orElseThrow();

        UserLabelEntity userLabelEntity = UserLabelEntity.from(userEntity, labelEntity);
        userLabelJpaRepository.save(userLabelEntity);
    }

    @Override
    public List<Label> findFirstComeLabels() {
        List<LabelEntity> labelEntities = userLabelJpaRepository.findFirstComeLabels(LabelType.FIRST_COME);
        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    public List<Label> findByLabelType(LabelType labelType) {
        List<LabelEntity> labelEntities = labelJpaRepository.findByLabelType(labelType);
        return LabelEntity.toLabels(labelEntities);
    }

    @Override
    public boolean existsUserLabelByUserAndLabel(User user, Label label) {
        return userLabelJpaRepository.existsByUserUserIdAndLabelLabelId(user.getUserId(), label.getLabelId());
    }
}

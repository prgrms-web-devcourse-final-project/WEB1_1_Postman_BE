package postman.bottler.label.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import postman.bottler.label.domain.Label;
import postman.bottler.label.domain.UserLabel;
import postman.bottler.label.exception.InvalidLabelException;
import postman.bottler.label.exception.UserLabelNotFoundException;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.infra.entity.UserLabelEntity;
import postman.bottler.label.service.LabelRepository;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.UserJpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

@Repository
@RequiredArgsConstructor
public class LabelRepositoryImpl implements LabelRepository {
//    @PersistenceContext
//    private EntityManager entityManager;

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
    public Label findLabelByLabelId(Long labelId) {
        LabelEntity labelEntity = labelJpaRepository.findByIdWithLock(labelId)
                .orElseThrow(() -> new InvalidLabelException("라벨 ID " + labelId + " 에 해당하는 라벨이 존재하지 않습니다."));
        return LabelEntity.toLabel(labelEntity);
    }

    @Override
    public void updateOwnedCount(Label label) {
//        label.increaseOwnedCount();
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
        labelEntity.updateOwnedCount();
    }

    @Override
    public void createUserLabel(User user, Label label) {
//        LabelEntity labelEntity = entityManager.find(LabelEntity.class, label.getLabelId());
        LabelEntity labelEntity = labelJpaRepository.findById(label.getLabelId())
                .orElseThrow();
//        UserEntity userEntity = entityManager.find(UserEntity.class, user.getUserId());
        UserEntity userEntity = userJpaRepository.findById(user.getUserId())
                .orElseThrow();

        UserLabelEntity userLabelEntity = UserLabelEntity.from(userEntity, labelEntity);
        userLabelJpaRepository.save(userLabelEntity);
    }

    @Override
    public List<UserLabel> findUserLabelByUserAndLabel(User user, Label label) {
        List<UserLabelEntity> userLabelEntities = userLabelJpaRepository.findLabelsByUserAndLabel(user.getUserId(), label.getLabelId());
        return UserLabelEntity.toUserLabels(userLabelEntities);
    }
}

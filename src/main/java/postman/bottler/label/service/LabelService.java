package postman.bottler.label.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.label.domain.Label;
import postman.bottler.label.domain.UserLabel;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.exception.DuplicateLabelException;
import postman.bottler.label.exception.FirstComeFirstServedLabelException;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.UserJpaRepository;
import postman.bottler.user.infra.entity.UserEntity;

@Service
public class LabelService {
    @PersistenceContext
    private EntityManager entityManager;

    private final LabelRepository labelRepository;
    private final UserJpaRepository userJpaRepository;

    public LabelService(LabelRepository labelRepository, UserJpaRepository userJpaRepository) {
        this.labelRepository = labelRepository;
        this.userJpaRepository = userJpaRepository;
    }

    @Transactional
    public void createLabel(String imageUrl, int limitCount) {
        labelRepository.save(Label.createLabel(imageUrl, limitCount));
    }

    @Transactional
    public List<LabelResponseDTO> findAllLabels() {
        List<Label> labels = labelRepository.findAllLabels();
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public List<LabelResponseDTO> findUserLabels(Long userId) {
        List<Label> labels = labelRepository.findLabelsByUser(userId);
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public void createFirstComeFirstServedLabel(Long labelId, Long userId) {
        //1. labelId에 해당하는 LabelEntity를 가져오면서 해당 라벨 Lock
        Label label = labelRepository.findLabelByLabelId(labelId);

        // TODO: 유저 로직 구현 후 바꿀 예정
        UserEntity userEntity = userJpaRepository.findById(userId).orElseThrow();
        entityManager.persist(userEntity);
        User user = UserEntity.toUser(userEntity);

        //2. 유저가 해당 라벨을 이미 가지고 있다면 예외 처리
        List<UserLabel> userLabel = labelRepository.findUserLabelByUserAndLabel(user, label);
        if (userLabel.size() >= 1) throw new DuplicateLabelException("이미 발급받은 라벨입니다.");

        //3. label에 최대 인원수와 소유 인원수를 비교한다.
        //3-1. 소유 인원수가 최대 인원수를 넘지 않는다면, 소유 인원수 업데이트 후 UserLabel에 저장
        if (label.isOwnedCountValid()) {
            labelRepository.updateOwnedCount(label);
            labelRepository.createUserLabel(user, label);
        } else { //3-2. 넘는다면, 선착순 마감 예외 처리
            throw new FirstComeFirstServedLabelException("선착순 뽑기 마감됐습니다.");
        }
    }
}
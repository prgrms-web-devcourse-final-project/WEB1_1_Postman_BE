package postman.bottler.label.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.label.application.dto.LabelRequestDTO;
import postman.bottler.label.application.repository.LabelRepository;
import postman.bottler.label.domain.Label;
import postman.bottler.label.domain.LabelType;
import postman.bottler.label.application.dto.LabelResponseDTO;
import postman.bottler.label.exception.FirstComeFirstServedLabelException;
import postman.bottler.scheduler.LabelScheduler;
import postman.bottler.user.domain.User;
import postman.bottler.user.application.service.UserService;

@Service
@RequiredArgsConstructor
public class LabelService {
    private final LabelRepository labelRepository;
    private final UserService userService;
    private final LabelScheduler labelScheduler;

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
    public LabelResponseDTO createFirstComeFirstServedLabel(Long userId) {
        User user = userService.findById(userId);

        List<Label> firstComeLabels = labelRepository.findByLabelType(LabelType.FIRST_COME);

        for (Label label : firstComeLabels) {
            boolean hasLabel = labelRepository.existsUserLabelByUserAndLabel(user, label);
            if (hasLabel) {
                continue;
            }

            if (label.isOwnedCountValid()) {
                labelRepository.updateOwnedCount(label);
                labelRepository.createUserLabel(user, label);
                return label.toLabelResponseDTO();
            }
        }

        throw new FirstComeFirstServedLabelException("모든 선착순 뽑기 라벨이 마감되었습니다.");
    }

    @Transactional
    public List<LabelResponseDTO> findFirstComeLabels() {
        List<Label> labels = labelRepository.findFirstComeLabels();
        return labels.stream().map(Label::toLabelResponseDTO).toList();
    }

    @Transactional
    public void updateFirstComeLabel(LabelRequestDTO labelRequestDTO) {
        labelScheduler.scheduleUpdateFirstComeLabel(labelRequestDTO);
    }
}

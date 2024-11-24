package postman.bottler.label.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import postman.bottler.label.controller.LabelService;
import postman.bottler.label.domain.Label;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.infra.entity.LabelEntity;

@Service
public class LabelServiceImpl implements LabelService {
    private final LabelRepository labelRepository;

    public LabelServiceImpl(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    @Override
    public void createLabel(String imageUrl) {
        labelRepository.save(Label.createLabel(imageUrl));
    }

    @Override
    public List<LabelResponseDTO> findAllLabels() {
        List<Label> labels =  labelRepository.findAllLabels();
        return Label.toResponseDTOList(labels);
    }

    @Override
    public void findOneLabel(Long letterId) {

    }

    @Override
    public void findUserLabels(Long userId) {

    }
}

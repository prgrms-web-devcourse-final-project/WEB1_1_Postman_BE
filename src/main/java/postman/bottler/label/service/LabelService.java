package postman.bottler.label.service;

import java.util.List;
import org.springframework.stereotype.Service;
import postman.bottler.label.domain.Label;
import postman.bottler.label.dto.response.LabelResponseDTO;

@Service
public class LabelService {
    private final LabelRepository labelRepository;

    public LabelService(LabelRepository labelRepository) {
        this.labelRepository = labelRepository;
    }

    public void createLabel(String imageUrl) {
        labelRepository.save(Label.createLabel(imageUrl));
    }

    public List<LabelResponseDTO> findAllLabels() {
        List<Label> labels =  labelRepository.findAllLabels();
        return Label.toResponseDTOList(labels);
    }
}

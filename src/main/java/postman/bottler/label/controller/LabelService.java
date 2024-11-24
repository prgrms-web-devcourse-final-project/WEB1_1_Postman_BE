package postman.bottler.label.controller;

import java.util.List;
import postman.bottler.label.dto.response.LabelResponseDTO;

public interface LabelService {
    void createLabel(String imageUrl);
    List<LabelResponseDTO> findAllLabels();
    void findOneLabel(Long letterId);
    void findUserLabels(Long userId);
}

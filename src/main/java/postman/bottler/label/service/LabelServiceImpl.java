package postman.bottler.label.service;

import org.springframework.stereotype.Service;
import postman.bottler.label.controller.LabelService;
import postman.bottler.label.domain.Label;

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
    public void findAllLabels() {

    }

    @Override
    public void findOneLabel(Long letterId) {

    }

    @Override
    public void findUserLabels(Long userId) {

    }
}

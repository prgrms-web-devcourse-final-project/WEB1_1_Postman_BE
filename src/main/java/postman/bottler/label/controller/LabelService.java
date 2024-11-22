package postman.bottler.label.controller;

public interface LabelService {
    void createLabel(String imageUrl);
    void findAllLabels();
    void findOneLabel(Long letterId);
    void findUserLabels(Long userId);
}

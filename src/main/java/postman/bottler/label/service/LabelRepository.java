package postman.bottler.label.service;

import java.util.List;
import postman.bottler.label.domain.Label;

public interface LabelRepository {
    void save(Label label);
    List<Label> findAllLabels();
}

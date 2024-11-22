package postman.bottler.label.service;

import postman.bottler.label.domain.Label;

public interface LabelRepository {
    void save(Label label);
}

package postman.bottler.label.service;

import java.util.List;
import postman.bottler.label.domain.Label;
import postman.bottler.user.domain.User;

public interface LabelRepository {
    void save(Label label);
    List<Label> findAllLabels();
    List<Label> findLabelsByUser(User user);
}

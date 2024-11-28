package postman.bottler.label.service;

import java.util.List;
import postman.bottler.label.domain.Label;
import postman.bottler.label.domain.UserLabel;
import postman.bottler.user.domain.User;

public interface LabelRepository {
    void save(Label label);
    List<Label> findAllLabels();
    List<Label> findLabelsByUser(Long userId);
    Label findLabelByLabelId(Long labelId);
    void updateOwnedCount(Label label);
    void createUserLabel(User user, Label label);
    List<UserLabel> findUserLabelByUserAndLabel(User user, Label label);
}

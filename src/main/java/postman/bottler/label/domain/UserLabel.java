package postman.bottler.label.domain;

import postman.bottler.user.domain.User;

public class UserLabel {
    private final User user;
    private final Label label;

    private UserLabel(User user, Label label) {
        this.user = user;
        this.label = label;
    }

    public User getUser() {
        return user;
    }

    public Label getLabel() {
        return label;
    }
}

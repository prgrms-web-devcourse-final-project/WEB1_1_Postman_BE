package postman.bottler.label.domain;

import postman.bottler.user.domain.User;

public class UserLabel {
    private User user;
    private Label label;

    private UserLabel(User user, Label label) {
        this.user = user;
        this.label = label;
    }

    public static UserLabel createUserLabel(User user, Label label) {
        return new UserLabel(user, label);
    }

    public User getUser() {
        return user;
    }

    public Label getLabel() {
        return label;
    }
}

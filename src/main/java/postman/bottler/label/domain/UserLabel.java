package postman.bottler.label.domain;

import lombok.Getter;
import postman.bottler.user.domain.User;

@Getter
public class UserLabel {
    private final User user;
    private final Label label;

    private UserLabel(User user, Label label) {
        this.user = user;
        this.label = label;
    }

    public static UserLabel createUserLabel(User user, Label label) {
        return new UserLabel(user, label);
    }
}

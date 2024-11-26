package postman.bottler.user.domain;

public class User {
    private String email;

    private User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

package postman.bottler.user.domain;

public class User {
    private final Long userId;
    private final String email;

    private User(Long userId, String email) {
        this.userId = userId;
        this.email = email;
    }

    public static User createUser(Long userId, String email) {
        return new User(userId, email);
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }
}

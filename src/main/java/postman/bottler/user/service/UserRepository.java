package postman.bottler.user.service;

import postman.bottler.user.domain.User;

public interface UserRepository {
    void save(User user);
    boolean findUserByEmail(String email);
    boolean findUserByNickname(String nickname);
    User findByEmail(String email);
}

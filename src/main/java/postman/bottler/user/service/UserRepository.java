package postman.bottler.user.service;

import postman.bottler.user.domain.User;

public interface UserRepository {
    void save(User user);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    User findByEmail(String email);
    void softDeleteUser(Long userId);
}

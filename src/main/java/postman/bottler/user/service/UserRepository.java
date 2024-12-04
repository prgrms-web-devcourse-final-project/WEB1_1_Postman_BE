package postman.bottler.user.service;

import java.util.List;
import postman.bottler.user.domain.User;

public interface UserRepository {
    void save(User user);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    User findByEmail(String email);
    void softDeleteUser(Long userId);
    void updateNickname(Long userId, String nickname);
    void updatePassword(Long userId, String password);
    void updateProfileImageUrl(Long userId, String imageUrl);
    User findById(Long userId);
    void unbanUsers(List<Long> ids);
}

package postman.bottler.user.application.repository;

import java.util.List;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.domain.User;

public interface UserRepository {
    User save(User user);

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    User findByEmail(String email);

    void softDeleteUser(Long userId);

    void updateNickname(Long userId, String nickname);

    void updatePassword(Long userId, String password);

    void updateProfileImageUrl(Long userId, String imageUrl);

    User findById(Long userId);

    void updateUsers(List<User> users);

    List<User> findWillBeUnbannedUsers(List<Ban> bans);

    boolean existsByEmailAndProvider(String kakaoId);

    List<User> findAllUserId();

    void updateWarningCount(User user);

    User findByNickname(String nickname);
}

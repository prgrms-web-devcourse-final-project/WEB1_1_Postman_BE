package postman.bottler.user.service;

import java.util.Optional;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserRepository {
    Optional<UserEntity> findOneByEmail(String email);
    void save(User user);
    boolean findUserByEmail(String email);
    boolean findUserByNickname(String nickname);
}

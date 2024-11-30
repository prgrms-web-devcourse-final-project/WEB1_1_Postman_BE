package postman.bottler.user.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.infra.entity.UserEntity;
import postman.bottler.user.service.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        userJpaRepository.save(UserEntity.from(user));
    }

    @Override
    public boolean findUserByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean findUserByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException("유저를 찾을 수 없습니다. " + email));
        return UserEntity.toUser(userEntity);
    }
}

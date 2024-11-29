package postman.bottler.user.infra;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.User;
import postman.bottler.user.infra.entity.UserEntity;
import postman.bottler.user.service.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<UserEntity> findOneByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public void save(User user) {
        userJpaRepository.save(UserEntity.from(user));
    }

    @Override
    public boolean findUserByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}

package postman.bottler.user.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.TokenException;
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
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }

    @Override
    public User findByEmail(String email) {
        UserEntity userEntity = userJpaRepository.findByEmail(email)
                .orElseThrow(() -> new EmailException("유저를 찾을 수 없습니다. " + email));
        return UserEntity.toUser(userEntity);
    }

    @Override
    public void softDeleteUser(Long userId) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new TokenException("해당 토큰에 대한 유저를 찾을 수 없습니다."));
        userEntity.updateIsDelete();
    }

    @Override
    public void updateNickname(Long userId, String nickname) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new TokenException("해당 토큰에 대한 유저를 찾을 수 없습니다."));
        userEntity.updateNickname(nickname);
    }

    @Override
    public void updatePassword(Long userId, String password) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new TokenException("해당 토큰에 대한 유저를 찾을 수 없습니다."));
        userEntity.updatePassword(password);
    }

    @Override
    public void updateProfileImageUrl(Long userId, String imageUrl) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new TokenException("해당 토큰에 대한 유저를 찾을 수 없습니다."));
        userEntity.updateImageUrl(imageUrl);
    }

    @Override
    public User findById(Long userId) {
        UserEntity userEntity = userJpaRepository.findById(userId)
                .orElseThrow(() -> new EmailException("유저를 찾을 수 없습니다. " + userId));
        return UserEntity.toUser(userEntity);
    }

    @Override
    public void unbanUsers(List<Long> ids) {
        userJpaRepository.unbanUsers(ids);
    }
}

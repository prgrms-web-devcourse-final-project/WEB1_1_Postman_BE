package postman.bottler.user.infra;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.domain.Provider;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.SingUpException;
import postman.bottler.user.exception.TokenException;
import postman.bottler.user.infra.entity.UserEntity;
import postman.bottler.user.application.repository.UserRepository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;

    @Override
    public User save(User user) {
        try {
            UserEntity userEntity = userJpaRepository.save(UserEntity.from(user));
            return UserEntity.toUser(userEntity);
        } catch (DataIntegrityViolationException e) {
            throw new SingUpException("이메일 또는 닉네임이 중복되었습니다.");
        }
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
    public void updateUsers(List<User> users) {
        List<UserEntity> userEntities = users.stream().map(UserEntity::from).toList();
        userJpaRepository.saveAll(userEntities);
    }

    @Override
    public List<User> findWillBeUnbannedUsers(List<Ban> bans) {
        List<Long> ids = bans.stream()
                .map(Ban::getUserId)
                .toList();
        return userJpaRepository.findByUserIdIn(ids).stream()
                .map(UserEntity::toUser)
                .toList();
    }

    public boolean existsByEmailAndProvider(String kakaoId) {
        return userJpaRepository.existsByEmailAndProvider(kakaoId, Provider.KAKAO);
    }

    @Override
    public List<User> findAllUserId() {
        List<UserEntity> userEntities = userJpaRepository.findAll();
        return userEntities.stream()
                .map(UserEntity::toUser)
                .collect(Collectors.toList());
    }

    @Override
    public void updateWarningCount(User user) {
        UserEntity userEntity = userJpaRepository.findById(user.getUserId())
                .orElseThrow(() -> new TokenException("해당 토큰에 대한 유저를 찾을 수 없습니다."));
        userEntity.updateBanUser(user);
    }

    @Override
    public User findByNickname(String nickname) {
        UserEntity userEntity = userJpaRepository.findByNickname(nickname)
                .orElseThrow(() -> new TokenException("해당 닉네임에 대한 유저를 찾을 수 없습니다."));
        return UserEntity.toUser(userEntity);
    }
}

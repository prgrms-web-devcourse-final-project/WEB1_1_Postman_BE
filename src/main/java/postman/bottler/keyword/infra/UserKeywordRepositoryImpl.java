package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.infra.entity.UserKeywordEntity;
import postman.bottler.keyword.service.UserKeywordRepository;

@Repository
@RequiredArgsConstructor
public class UserKeywordRepositoryImpl implements UserKeywordRepository {

    private final UserKeywordJpaRepository userKeywordJpaRepository;

    @Override
    public List<UserKeyword> findAllByUserId(Long userId) {
        List<UserKeywordEntity> userKeywordEntities = userKeywordJpaRepository.findAllByUserId(userId);
        return userKeywordEntities.stream()
                .map(UserKeywordEntity::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<UserKeyword> userKeywords) {
        List<UserKeywordEntity> userKeywordEntities = userKeywords.stream()
                .map(UserKeywordEntity::from)
                .toList();
        userKeywordJpaRepository.saveAll(userKeywordEntities);
    }

    @Override
    public void deleteAllByUserId(Long userId) {
        userKeywordJpaRepository.deleteAllByUserId(userId);
    }
}

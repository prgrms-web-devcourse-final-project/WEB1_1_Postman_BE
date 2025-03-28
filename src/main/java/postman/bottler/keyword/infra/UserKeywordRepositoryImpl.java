package postman.bottler.keyword.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.application.repository.UserKeywordRepository;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.infra.entity.UserKeywordEntity;

@Repository
@RequiredArgsConstructor
public class UserKeywordRepositoryImpl implements UserKeywordRepository {

    private final UserKeywordJpaRepository jpaRepository;
    private final UserKeywordJdbcRepository jdbcRepository;

    @Override
    public List<UserKeyword> findUserKeywordsByUserId(Long userId) {
        return jpaRepository.findAllByUserId(userId).stream().map(UserKeywordEntity::toDomain).toList();
    }

    @Override
    public void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId) {
        jdbcRepository.deleteAllByUserId(userId);
        jdbcRepository.batchInsertKeywords(userKeywords);
    }

    @Override
    public List<String> findKeywordsByUserId(Long userId) {
        return jpaRepository.findKeywordsByUserId(userId);
    }
}

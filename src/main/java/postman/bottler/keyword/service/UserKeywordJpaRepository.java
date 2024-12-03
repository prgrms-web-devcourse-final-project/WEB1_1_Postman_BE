package postman.bottler.keyword.service;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.infra.entity.UserKeywordEntity;

public interface UserKeywordJpaRepository extends JpaRepository<UserKeyword, Long> {
    List<UserKeywordEntity> findAllByUserId(Long userId);
}

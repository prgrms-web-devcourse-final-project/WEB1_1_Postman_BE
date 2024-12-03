package postman.bottler.keyword.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.keyword.infra.entity.UserKeywordEntity;

public interface UserKeywordJpaRepository extends JpaRepository<UserKeywordEntity, Long> {
    List<UserKeywordEntity> findAllByUserId(Long userId);

    void deleteAllByUserId(Long userId);
}

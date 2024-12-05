package postman.bottler.keyword.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.keyword.infra.entity.KeywordEntity;

public interface KeywordJpaRepository extends JpaRepository<KeywordEntity, Long> {
}

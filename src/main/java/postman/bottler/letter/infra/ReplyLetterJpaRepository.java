package postman.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.infra.entity.ReplyLetterEntity;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetterEntity, Long> {
}

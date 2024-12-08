package postman.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.infra.entity.LetterBoxEntity;

public interface LetterBoxJpaRepository extends JpaRepository<LetterBoxEntity, Long> {
}

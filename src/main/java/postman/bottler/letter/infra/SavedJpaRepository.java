package postman.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.infra.entity.SavedLetterEntity;

public interface SavedJpaRepository extends JpaRepository<SavedLetterEntity, Long> {
    boolean existsByUserIdAndLetterId(Long userId, Long letterId);
}

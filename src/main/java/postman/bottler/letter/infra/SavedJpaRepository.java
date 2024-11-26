package postman.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.domain.SavedLetter;

public interface SavedJpaRepository extends JpaRepository<SavedLetter, Long> {
}

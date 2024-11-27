package postman.bottler.letter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.domain.ReplyLetter;

public interface ReplyLetterJpaRepository extends JpaRepository<ReplyLetter, Long> {
}

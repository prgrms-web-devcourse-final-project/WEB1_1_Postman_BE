package postman.bottler.letter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.letter.infra.entity.LetterEntity;

public interface LetterJpaRepository extends JpaRepository<LetterEntity, Long> {
    Page<LetterEntity> findAllByUserId(Long userId, Pageable pageable);
}

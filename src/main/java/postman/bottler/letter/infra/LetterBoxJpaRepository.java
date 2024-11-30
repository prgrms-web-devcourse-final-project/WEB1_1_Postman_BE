package postman.bottler.letter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.letter.infra.entity.LetterBoxEntity;
import postman.bottler.letter.infra.entity.LetterEntity;

public interface LetterBoxJpaRepository extends JpaRepository<LetterBoxEntity, Long> {

    @Query("""
            SELECT l
            FROM LetterBoxEntity s
            JOIN LetterEntity l ON s.letterId = l.id
            WHERE s.userId = :userId
            """)
    Page<LetterEntity> findSavedLettersByUserId(@Param("userId") Long userId, Pageable pageable);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    void deleteByUserIdAndLetterId(Long userId, Long letterId);
}

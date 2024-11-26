package postman.bottler.letter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.letter.infra.entity.LetterEntity;
import postman.bottler.letter.infra.entity.SavedLetterEntity;

public interface SavedJpaRepository extends JpaRepository<SavedLetterEntity, Long> {

    @Query("""
            SELECT l
            FROM SavedLetterEntity s
            JOIN LetterEntity l ON s.letterId = l.id
            WHERE s.userId = :userId
            """)
    Page<LetterEntity> findSavedLettersByUserId(@Param("userId") Long userId, Pageable pageable);

    boolean existsByUserIdAndLetterId(Long userId, Long letterId);

    void deleteByUserIdAndLetterId(Long userId, Long letterId);
}

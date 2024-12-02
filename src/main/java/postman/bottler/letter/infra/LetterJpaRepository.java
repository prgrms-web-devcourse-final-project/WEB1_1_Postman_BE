package postman.bottler.letter.infra;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.letter.infra.entity.LetterEntity;

public interface LetterJpaRepository extends JpaRepository<LetterEntity, Long> {
    Page<LetterEntity> findAllByUserId(Long userId, Pageable pageable);

    boolean existsByUserIdAndId(Long userId, Long id);

    @Modifying
    @Query("DELETE FROM LetterEntity r WHERE r.id IN :letterIds")
    void deleteByIds(List<Long> letterIds);
}

package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.letter.infra.entity.LetterEntity;

public interface LetterJpaRepository extends JpaRepository<LetterEntity, Long> {

    @Query("SELECT l FROM LetterEntity l WHERE l.id = :id AND l.isDeleted = false")
    Optional<LetterEntity> findActiveById(Long id);

    @Modifying
    @Query("UPDATE LetterEntity l SET l.isDeleted = true WHERE l.id IN :ids")
    void deleteByIds(List<Long> ids);
}

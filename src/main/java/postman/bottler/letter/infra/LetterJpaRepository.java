package postman.bottler.letter.infra;

import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.letter.infra.entity.LetterEntity;

public interface LetterJpaRepository extends JpaRepository<LetterEntity, Long> {

    @Override
    @NotNull
    @Query("SELECT l FROM LetterEntity l WHERE l.id = :id AND l.isDeleted = false")
    Optional<LetterEntity> findById(@NotNull Long id);

    @Query("SELECT l FROM LetterEntity l WHERE l.userId = :userId AND l.isDeleted = false")
    List<LetterEntity> findAllByUserId(Long userId);

    @Query("SELECT l FROM LetterEntity l WHERE l.id IN :letterIds AND l.isDeleted = false")
    List<LetterEntity> findAllByIds(List<Long> letterIds);

    @Modifying
    @Query("UPDATE LetterEntity l SET l.isDeleted = true WHERE l.id IN :ids")
    void softDeleteByIds(List<Long> ids);

    @Modifying
    @Query("UPDATE LetterEntity l SET l.isBlocked = true, l.isDeleted = true WHERE l.id = :id")
    void softBlockById(Long id);
}

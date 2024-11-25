package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.infra.entity.PaperEntity;

@Repository
public interface PaperJpaRepository extends JpaRepository<PaperEntity, Long> {
}

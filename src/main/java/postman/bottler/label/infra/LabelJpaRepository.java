package postman.bottler.label.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.label.infra.entity.LabelEntity;

public interface LabelJpaRepository extends JpaRepository<LabelEntity, Long> {

}

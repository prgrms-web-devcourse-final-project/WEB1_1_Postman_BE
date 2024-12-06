package postman.bottler.user.infra;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import postman.bottler.user.infra.entity.BanEntity;

public interface JpaBanRepository extends JpaRepository<BanEntity, Long> {
    List<BanEntity> findByUnbansAtBefore(LocalDateTime now);
}

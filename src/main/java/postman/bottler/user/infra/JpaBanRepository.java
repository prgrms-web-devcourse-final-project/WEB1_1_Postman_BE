package postman.bottler.user.infra;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import postman.bottler.user.infra.entity.BanEntity;

public interface JpaBanRepository extends JpaRepository<BanEntity, Long> {
    List<BanEntity> findByUnbansAtBefore(LocalDateTime now);

    @Modifying
    @Query("delete from BanEntity b where b.userId in :ids")
    void deleteByIds(List<Long> ids);
}

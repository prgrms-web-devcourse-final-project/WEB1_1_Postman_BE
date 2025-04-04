package postman.bottler.label.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.label.domain.LabelType;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.infra.entity.UserLabelEntity;

public interface UserLabelJpaRepository extends JpaRepository<UserLabelEntity, Long> {
    @Query("SELECT ul.label FROM UserLabelEntity ul WHERE ul.user.userId = :userId")
    List<LabelEntity> findLabelsByUserId(@Param("userId") Long userId);

    @Query("SELECT l FROM LabelEntity l WHERE l.labelType = :labelType")
    List<LabelEntity> findFirstComeLabels(@Param("labelType") LabelType labelType);

    boolean existsByUserUserIdAndLabelLabelId(Long userId, Long labelId);
}

package postman.bottler.label.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.label.infra.entity.LabelEntity;
import postman.bottler.label.infra.entity.UserLabelEntity;
import postman.bottler.user.infra.entity.UserEntity;

public interface UserLabelJpaRepository extends JpaRepository<UserLabelEntity, Long> {
    @Query("SELECT ul.label FROM UserLabelEntity ul WHERE ul.user = :user")
    List<LabelEntity> findLabelsByUser(@Param("user") UserEntity user);
}

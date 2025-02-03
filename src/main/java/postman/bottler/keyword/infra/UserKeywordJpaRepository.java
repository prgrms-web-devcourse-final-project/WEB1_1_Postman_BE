package postman.bottler.keyword.infra;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import postman.bottler.keyword.infra.entity.UserKeywordEntity;

public interface UserKeywordJpaRepository extends JpaRepository<UserKeywordEntity, Long> {
    List<UserKeywordEntity> findAllByUserId(Long userId);

    @Query("SELECT uk.keyword FROM UserKeywordEntity uk WHERE uk.userId = :userId")
    List<String> findKeywordsByUserId(@Param("userId") Long userId);
}

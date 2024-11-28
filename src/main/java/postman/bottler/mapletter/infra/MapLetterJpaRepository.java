package postman.bottler.mapletter.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MapLetterJpaRepository extends JpaRepository<MapLetterEntity, Long> {
    @Query("SELECT m FROM MapLetterEntity m WHERE m.createUserId = :userId AND m.isDeleted = false AND m.isBlocked = false")
    List<MapLetterEntity> findActiveByCreateUserId(Long userId);

    @Query("SELECT m FROM MapLetterEntity m WHERE m.targetUserId = :userId AND m.isDeleted = false AND m.isBlocked=false")
    List<MapLetterEntity> findActiveByTargetUserId(Long userId);

    @Query(value = "SELECT m.map_letter_id as letterId, m.latitude, m.longitude, m.title, m.description, " +
            "m.created_at as createdAt, m.target_user_id as targetUserId, m.create_user_id as createUserId, m.label, " +
            "st_distance_sphere(point(m.longitude, m.latitude), point( :longitude, :latitude)) AS distance " +
            "FROM map_letter_tb m " +
            "WHERE (m.type = 'PUBLIC'  OR (m.type = 'PRIVATE' AND m.target_user_id =:targetUserId)) " +
            "AND st_distance_sphere(point(m.longitude, m.latitude), point( :longitude, :latitude)) <= 500 " +
            "AND m.is_deleted =false AND m.is_blocked=false " +
            "AND TIMESTAMPDIFF(DAY, m.created_at, NOW()) <= 30", nativeQuery = true)
    List<MapLetterAndDistance> findLettersByUserLocation(@Param("latitude") BigDecimal latitude,
                                                         @Param("longitude") BigDecimal longitude,
                                                         @Param("targetUserId") Long targetUserId);


    @Query("SELECT m FROM MapLetterEntity m WHERE m.isDeleted=false AND m.isBlocked=false " +
            "AND m.mapLetterId = :sourceMapLetterId")
    MapLetterEntity findActiveById(Long sourceMapLetterId);

    @Modifying
    @Query("UPDATE MapLetterEntity m SET m.isBlocked = true WHERE m.mapLetterId = :letterId")
    void letterBlock(Long letterId);
}

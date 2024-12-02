package postman.bottler.mapletter.infra;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MapLetterJpaRepository extends JpaRepository<MapLetterEntity, Long> {
    @Query("SELECT m FROM MapLetterEntity m "
            + "WHERE m.createUserId = :userId AND m.isDeleted = false AND m.isBlocked = false ORDER BY m.createdAt DESC ")
    Page<MapLetterEntity> findActiveByCreateUserId(Long userId, Pageable pageable);

    @Query("SELECT m FROM MapLetterEntity m "
            + "WHERE m.targetUserId = :userId AND m.isDeleted = false AND m.isBlocked=false")
    List<MapLetterEntity> findActiveByTargetUserId(Long userId);

    @Query(value = "SELECT m.map_letter_id as letterId, m.latitude, m.longitude, m.title, m.description, "
            + "m.created_at as createdAt, m.target_user_id as targetUserId, m.create_user_id as createUserId, m.label, "
            + "st_distance_sphere(point(m.longitude, m.latitude), point( :longitude, :latitude)) AS distance "
            + "FROM map_letter m "
            + "WHERE (m.type = 'PUBLIC'  OR (m.type = 'PRIVATE' AND m.target_user_id =:targetUserId)) "
            + "AND st_distance_sphere(point(m.longitude, m.latitude), point( :longitude, :latitude)) <= 500 "
            + "AND m.is_deleted =false AND m.is_blocked=false "
            + "AND TIMESTAMPDIFF(DAY, m.created_at, NOW()) <= 30", nativeQuery = true)
    List<MapLetterAndDistance> findLettersByUserLocation(@Param("latitude") BigDecimal latitude,
                                                         @Param("longitude") BigDecimal longitude,
                                                         @Param("targetUserId") Long targetUserId);


    @Query("SELECT m FROM MapLetterEntity m WHERE m.isDeleted=false AND m.isBlocked=false "
            + "AND m.mapLetterId = :sourceMapLetterId")
    MapLetterEntity findActiveById(Long sourceMapLetterId);

    @Modifying
    @Query("UPDATE MapLetterEntity m SET m.isBlocked = true WHERE m.mapLetterId = :letterId")
    void letterBlock(Long letterId);

    @Query(value =
            "SELECT st_distance_sphere(point(m.longitude, m.latitude), point( :longitude, :latitude)) AS distance "
                    + "FROM map_letter m "
                    + "WHERE m.is_deleted =false AND m.is_blocked=false "
                    + "AND TIMESTAMPDIFF(DAY, m.created_at, NOW()) <= 30 "
                    + "AND m.map_letter_id=:letterId", nativeQuery = true)
    Double findDistanceByLatitudeAndLongitudeAndLetterId(BigDecimal latitude, BigDecimal longitude, Long letterId);

    @Query(value = """
            SELECT m.map_letter_id AS letterId,  m.title AS title, m.description AS description, m.label AS label, 
                   NULL AS targetUserNickname, 
                   CASE 
                       WHEN m.type = 'PRIVATE' THEN 'TARGET' 
                       WHEN m.type = 'PUBLIC' THEN 'PUBLIC' 
                   END AS type, 
                   m.created_at AS createdAt, NULL as sourceLetterId 
            FROM map_letter m 
            WHERE m.create_user_id = :userId AND m.is_deleted = false AND m.is_blocked = false 
            UNION ALL 
            SELECT r.reply_letter_id AS letterId, 
                   CONCAT('Re: ', (SELECT ml.title FROM map_letter ml WHERE ml.map_letter_id = r.source_letter_id)) AS title, 
                   NULL AS description, r.label AS label, NULL AS targetUserNickname, 'REPLY' AS type,
                   r.created_at AS createdAt, r.source_letter_id AS sourceLetterId 
            FROM reply_map_letter r 
            WHERE r.create_user_id = :userId AND r.is_deleted = false AND r.is_blocked = false 
            ORDER BY createdAt DESC
            """,
            countQuery = """
                    SELECT COUNT(*) 
                    FROM (
                        SELECT m.map_letter_id FROM map_letter m 
                        WHERE m.create_user_id = :userId AND m.is_deleted = false AND m.is_blocked = false 
                        UNION ALL 
                        SELECT r.reply_letter_id FROM reply_map_letter r 
                        WHERE r.create_user_id = :userId AND r.is_deleted = false AND r.is_blocked = false
                    ) AS combined
                    """,
            nativeQuery = true)
    Page<FindSentMapLetter> findSentLettersByUserId(Long userId, Pageable pageable);

    @Query(value = "SELECT m.map_letter_id AS letterId, m.title AS title, m.description AS description, "
            + "m.latitude as latitude, m.longitude as longitude, " +
            "m.label AS label, NULL AS sourceLetterId, 'TARGET' AS type, m.created_at AS createdAt " +
            "FROM map_letter m " +
            "WHERE m.target_user_id = :userId AND m.is_deleted = false AND m.is_blocked = false " +
            "UNION ALL " +
            "SELECT r.reply_letter_id AS letterId, "
            + "CONCAT('Re: ', (SELECT ml.title FROM map_letter ml WHERE ml.map_letter_id = r.source_letter_id)) AS title, " +
            "NULL AS description, NULL AS latitude, NULL AS longitude, "
            + "r.label AS label, r.source_letter_id AS sourceLetterId, 'REPLY' AS type, r.created_at AS createdAt " +
            "FROM reply_map_letter r " +
            "WHERE r.create_user_id = :userId AND r.is_deleted = false AND r.is_blocked = false " +
            "ORDER BY createdAt DESC",
            countQuery = "SELECT COUNT(*) FROM (" +
                    "SELECT m.map_letter_id FROM map_letter m " +
                    "WHERE m.target_user_id = :userId AND m.is_deleted = false AND m.is_blocked = false " +
                    "UNION ALL " +
                    "SELECT r.reply_letter_id FROM reply_map_letter r " +
                    "WHERE r.create_user_id = :userId AND r.is_deleted = false AND r.is_blocked = false) AS combined",
            nativeQuery = true)
    Page<FindReceivedMapLetterDTO> findActiveReceivedMapLettersByUserId(Long userId, PageRequest pageRequest);
}

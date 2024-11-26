package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface MapLetterRepository {
    MapLetter save(MapLetter mapLetter);
    MapLetter findById(Long id);

    void softDelete(Long letterId);

    List<MapLetter> findActiveByCreateUserId(Long userId);

    List<MapLetter> findActiveByTargetUserId(Long userId);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);
}

package postman.bottler.mapletter.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;

import java.math.BigDecimal;
import java.util.List;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

@Repository
public interface MapLetterRepository {
    MapLetter save(MapLetter mapLetter);

    MapLetter findById(Long id);

    void softDelete(Long letterId);

    Page<MapLetter> findActiveByCreateUserId(Long userId, Pageable pageable);

    List<MapLetter> findActiveByTargetUserId(Long userId);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);

    void findSourceMapLetterById(Long sourceMapLetterId);

    void letterBlock(Long letterId);

    Double findDistanceByLatitudeAndLongitudeAndLetterId(BigDecimal latitude, BigDecimal longitude, Long letterId);

    Page<FindSentMapLetter> findSentLettersByUserId(Long userId, Pageable createdAt);

    Page<FindReceivedMapLetterDTO> findActiveReceivedMapLettersByUserId(Long userId, PageRequest pageRequest);
}

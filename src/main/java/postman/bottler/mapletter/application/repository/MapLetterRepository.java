package postman.bottler.mapletter.application.repository;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;

@Repository
public interface MapLetterRepository {
    MapLetter save(MapLetter mapLetter);

    MapLetter findById(Long id);

    void softDelete(Long letterId);

    Page<MapLetter> findActiveByCreateUserId(Long userId, Pageable pageable);

    Page<MapLetter> findActiveByTargetUserId(Long userId, Pageable pageable);

    List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId);

    MapLetter findSourceMapLetterById(Long sourceMapLetterId);

    void letterBlock(Long letterId);

    Double findDistanceByLatitudeAndLongitudeAndLetterId(BigDecimal latitude, BigDecimal longitude, Long letterId);

    Page<FindSentMapLetter> findSentLettersByUserId(Long userId, Pageable createdAt);

    Page<FindReceivedMapLetterDTO> findActiveReceivedMapLettersByUserId(Long userId, PageRequest pageRequest);

    List<MapLetterAndDistance> guestFindLettersByUserLocation(BigDecimal latitude, BigDecimal longitude);

    List<MapLetter> findAllByIds(List<Long> ids);
}

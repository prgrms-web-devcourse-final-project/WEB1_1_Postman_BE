package postman.bottler.mapletter.infra;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.exception.SourceMapLetterNotFountException;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;
import postman.bottler.mapletter.service.MapLetterRepository;

@Repository
@RequiredArgsConstructor
public class MapLetterRepositoryImpl implements MapLetterRepository {
    private final MapLetterJpaRepository mapLetterJpaRepository;
    private final EntityManager em;

    @Override
    public MapLetter save(MapLetter mapLetter) {
        MapLetterEntity mapLetterEntity = MapLetterEntity.from(mapLetter);
        MapLetterEntity save = mapLetterJpaRepository.save(mapLetterEntity);
        return MapLetterEntity.toDomain(save);
    }

    @Override
    public MapLetter findById(Long id) {
        MapLetterEntity mapLetter = mapLetterJpaRepository.findById(id)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));
        return MapLetterEntity.toDomain(mapLetter);
    }

    @Override
    public void softDelete(Long letterId) {
        MapLetterEntity letter = mapLetterJpaRepository.findById(letterId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        letter.updateDelete(true);
    }

    @Override
    public Page<MapLetter> findActiveByCreateUserId(Long userId, Pageable pageable) {
        Page<MapLetterEntity> findActiveLetters = mapLetterJpaRepository.findActiveByCreateUserId(userId, pageable);

        return findActiveLetters.map(MapLetterEntity::toDomain);
    }

    @Override
    public List<MapLetter> findActiveByTargetUserId(Long userId) {
        List<MapLetterEntity> findActiveLetters = mapLetterJpaRepository.findActiveByTargetUserId(userId);

        return findActiveLetters.stream()
                .map(MapLetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude,
                                                                Long userId) {
        return mapLetterJpaRepository.findLettersByUserLocation(latitude, longitude, userId);
    }

    @Override
    public void findSourceMapLetterById(Long sourceMapLetterId) {
        MapLetterEntity activeLetter = mapLetterJpaRepository.findActiveById(sourceMapLetterId);
        if (activeLetter == null) {
            throw new SourceMapLetterNotFountException("원본 편지를 찾을 수 없습니다. 편지가 존재하지 않거나 삭제되었습니다.");
        }
    }

    @Override
    public void letterBlock(Long letterId) {
        mapLetterJpaRepository.letterBlock(letterId);
    }

    @Override
    public Double findDistanceByLatitudeAndLongitudeAndLetterId(BigDecimal latitude, BigDecimal longitude,
                                                                 Long letterId) {
        return mapLetterJpaRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude, letterId);
    }

    @Override
    public Page<FindSentMapLetter> findSentLettersByUserId(Long userId, Pageable pageable) {
        return mapLetterJpaRepository.findSentLettersByUserId(userId, pageable);
    }
}

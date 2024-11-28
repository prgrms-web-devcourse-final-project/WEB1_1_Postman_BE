package postman.bottler.mapletter.infra;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetter;
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
    @Transactional
    public MapLetter save(MapLetter mapLetter) {
        MapLetterEntity mapLetterEntity=MapLetterEntity.from(mapLetter);
        MapLetterEntity save = mapLetterJpaRepository.save(mapLetterEntity);
        return MapLetterEntity.toDomain(save);
    }

    @Override
    public MapLetter findById(Long id) {
        MapLetterEntity mapLetter = mapLetterJpaRepository.findById(id)
                .orElseThrow(()->new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));
        return MapLetterEntity.toDomain(mapLetter);
    }

    @Override
    @Transactional
    public void softDelete(Long letterId) {
        MapLetterEntity letter = mapLetterJpaRepository.findById(letterId)
                .orElseThrow(()->new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        MapLetterEntity mapLetter = em.find(MapLetterEntity.class, letterId);
        mapLetter.updateDelete(true);
    }

    @Override
    public List<MapLetter> findActiveByCreateUserId(Long userId) {
        List<MapLetterEntity> findActiveLetters = mapLetterJpaRepository.findActiveByCreateUserId(userId);

        return findActiveLetters.stream()
                .map(MapLetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<MapLetter> findActiveByTargetUserId(Long userId) {
        List<MapLetterEntity> findActiveLetters = mapLetterJpaRepository.findActiveByTargetUserId(userId);

        return findActiveLetters.stream()
                .map(MapLetterEntity::toDomain)
                .toList();
    }

    @Override
    public List<MapLetterAndDistance> findLettersByUserLocation(BigDecimal latitude, BigDecimal longitude, Long userId) {
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
}

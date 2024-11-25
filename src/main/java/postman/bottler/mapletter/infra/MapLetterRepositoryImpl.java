package postman.bottler.mapletter.infra;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.response.OneLetterResponse;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;
import postman.bottler.mapletter.service.MapLetterRepository;

@Repository
@RequiredArgsConstructor
public class MapLetterRepositoryImpl implements MapLetterRepository {
    private final MapLetterJpaRepository mapLetterJpaRepository;

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
    public void delete(Long letterId) {
        mapLetterJpaRepository.deleteById(letterId);
    }
}

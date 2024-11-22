package postman.bottler.mapletter.infra;

import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetter;
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
}

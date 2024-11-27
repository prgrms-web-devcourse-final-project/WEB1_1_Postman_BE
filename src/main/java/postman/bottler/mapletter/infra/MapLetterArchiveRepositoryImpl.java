package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;
import postman.bottler.mapletter.service.MapLetterArchiveRepository;

@Repository
@RequiredArgsConstructor
public class MapLetterArchiveRepositoryImpl implements MapLetterArchiveRepository {
    private final MapLetterArchiveJpaRepository mapLetterArchiveJpaRepository;

    @Override
    public MapLetterArchive save(MapLetterArchive archive) {
        MapLetterArchiveEntity mapLetterArchiveEntity=MapLetterArchiveEntity.from(archive);
        MapLetterArchiveEntity save = mapLetterArchiveJpaRepository.save(mapLetterArchiveEntity);
        return MapLetterArchiveEntity.toDomain(save);
    }
}

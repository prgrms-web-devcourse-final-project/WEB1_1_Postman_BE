package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;
import postman.bottler.mapletter.service.MapLetterArchiveRepository;

import java.util.List;

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

    @Override
    public List<FindAllArchiveLetters> findAllById(Long userId) {
        return mapLetterArchiveJpaRepository.findAllByUserId(userId);
    }
}

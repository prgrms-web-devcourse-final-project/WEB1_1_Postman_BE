package postman.bottler.mapletter.infra;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.application.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;
import postman.bottler.mapletter.application.repository.MapLetterArchiveRepository;

@Repository
@RequiredArgsConstructor
public class MapLetterArchiveRepositoryImpl implements MapLetterArchiveRepository {
    private final MapLetterArchiveJpaRepository mapLetterArchiveJpaRepository;

    @Override
    public MapLetterArchive save(MapLetterArchive archive) {
        MapLetterArchiveEntity mapLetterArchiveEntity = MapLetterArchiveEntity.from(archive);
        MapLetterArchiveEntity save = mapLetterArchiveJpaRepository.save(mapLetterArchiveEntity);
        return MapLetterArchiveEntity.toDomain(save);
    }

    @Override
    public Page<FindAllArchiveLetters> findAllById(Long userId, Pageable pageable) {
        return mapLetterArchiveJpaRepository.findAllByUserId(userId, pageable);
    }

    @Override
    public MapLetterArchive findById(Long archiveId) {
        return MapLetterArchiveEntity.toDomain(mapLetterArchiveJpaRepository.findById(archiveId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다.")));
    }

    @Override
    public void deleteById(Long archiveId) {
        MapLetterArchiveEntity archive = mapLetterArchiveJpaRepository.findById(archiveId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        mapLetterArchiveJpaRepository.delete(archive);
    }

    @Override
    public boolean findByLetterIdAndUserId(Long letterId, Long userId) {
        return mapLetterArchiveJpaRepository.findByMapLetterIdAndUserId(letterId, userId).isPresent();
        // 값이 없으면 false
    }
}

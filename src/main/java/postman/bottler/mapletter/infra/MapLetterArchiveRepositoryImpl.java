package postman.bottler.mapletter.infra;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.infra.entity.MapLetterArchiveEntity;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;
import postman.bottler.mapletter.service.MapLetterArchiveRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MapLetterArchiveRepositoryImpl implements MapLetterArchiveRepository {
    private final MapLetterArchiveJpaRepository mapLetterArchiveJpaRepository;
    private final EntityManager em;

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

    @Override
    public MapLetterArchive findById(Long archiveId) {
        return MapLetterArchiveEntity.toDomain(mapLetterArchiveJpaRepository.findById(archiveId)
                .orElseThrow(() -> new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다.")));
    }

    @Override
    @Transactional
    public void deleteById(Long archiveId) {
        MapLetterArchiveEntity archive = mapLetterArchiveJpaRepository.findById(archiveId)
                .orElseThrow(()->new MapLetterNotFoundException("해당 편지를 찾을 수 없습니다."));

        mapLetterArchiveJpaRepository.delete(archive);
    }

    @Override
    public boolean findByLetterIdAndUserId(Long letterId, Long userId) {
        return mapLetterArchiveJpaRepository.findByMapLetterIdAndUserId(letterId, userId).isPresent();
        // 값이 없으면 false
    }
}

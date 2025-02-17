package postman.bottler.mapletter.application.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.application.dto.response.FindAllArchiveLetters;

@Repository
public interface MapLetterArchiveRepository {
    MapLetterArchive save(MapLetterArchive archive);

    Page<FindAllArchiveLetters> findAllById(Long userId, Pageable pageable);

    MapLetterArchive findById(Long letterId);

    void deleteById(Long letterId);

    boolean findByLetterIdAndUserId(Long letterId, Long userId);

    List<MapLetterArchive> findAllById(List<Long> letterIds);

    void deleteAllByIdInBatch(List<Long> letterIds);
}

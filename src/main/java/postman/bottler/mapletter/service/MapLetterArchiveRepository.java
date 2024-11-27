package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;

import java.util.List;

@Repository
public interface MapLetterArchiveRepository {
    MapLetterArchive save(MapLetterArchive archive);

    List<FindAllArchiveLetters> findAllById(Long userId);

    MapLetterArchive findById(Long letterId);

    void deleteById(Long letterId);
}

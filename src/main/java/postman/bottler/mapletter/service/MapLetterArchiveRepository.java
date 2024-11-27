package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetterArchive;

@Repository
public interface MapLetterArchiveRepository {
    MapLetterArchive save(MapLetterArchive archive);
}

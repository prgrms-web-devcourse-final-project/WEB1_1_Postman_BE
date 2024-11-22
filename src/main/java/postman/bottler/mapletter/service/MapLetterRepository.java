package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;

@Repository
public interface MapLetterRepository {
    MapLetter save(MapLetter mapLetter);
}

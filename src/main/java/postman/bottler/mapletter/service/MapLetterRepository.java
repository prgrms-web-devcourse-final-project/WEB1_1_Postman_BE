package postman.bottler.mapletter.service;

import org.springframework.stereotype.Repository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.response.OneLetterResponse;

import java.util.Optional;

@Repository
public interface MapLetterRepository {
    MapLetter save(MapLetter mapLetter);
    MapLetter findById(Long id);
}

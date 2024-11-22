package postman.bottler.mapletter.controller;

import org.springframework.stereotype.Service;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;

@Service
public interface MapLetterService {
    MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId);
}

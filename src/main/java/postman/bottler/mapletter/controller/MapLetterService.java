package postman.bottler.mapletter.controller;

import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

@Service
public interface MapLetterService {
    MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId);
}

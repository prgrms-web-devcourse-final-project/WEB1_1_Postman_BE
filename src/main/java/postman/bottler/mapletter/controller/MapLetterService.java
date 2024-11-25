package postman.bottler.mapletter.controller;

import org.springframework.stereotype.Service;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.OneLetterResponse;

@Service
public interface MapLetterService {
    MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId);
    MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO, Long userId);
    OneLetterResponse findOneMepLetter(Long letterId, Long userId);
}

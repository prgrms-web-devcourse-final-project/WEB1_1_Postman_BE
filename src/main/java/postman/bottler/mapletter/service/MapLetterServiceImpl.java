package postman.bottler.mapletter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.mapletter.controller.MapLetterService;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;

@Service
@RequiredArgsConstructor
public class MapLetterServiceImpl implements MapLetterService {
    private final MapLetterRepository mapLetterRepository;

    @Override
    public MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId) {
        MapLetter mapLetter = MapLetter.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }
}

package postman.bottler.mapletter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindMapLetter;
import postman.bottler.mapletter.dto.response.OneLetterResponse;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
//    private final UserService userService;

    public MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId) {
        MapLetter mapLetter = MapLetter.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    public MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO, Long userId) {
        MapLetter mapLetter = MapLetter.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    public OneLetterResponse findOneMepLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if (mapLetter.getType() == MapLetterType.PRIVATE && !mapLetter.getTargetUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }

        OneLetterResponse oneLetterResponse = MapLetter.toOneLetterResponse(mapLetter);
        String profileImg = ""; //user 서비스 메서드 불러서 받기
        return OneLetterResponse.builder()
                .title(oneLetterResponse.title())
                .content(oneLetterResponse.content())
                .profileImg(profileImg)
                .font(oneLetterResponse.font())
                .paper(oneLetterResponse.paper())
                .label(oneLetterResponse.label())
                .createdAt(oneLetterResponse.createdAt())
                .build();
    }

    public void deleteMapLetter(Long letterId, Long userId) {
        MapLetter findMapLetter = mapLetterRepository.findById(letterId);
        if(!findMapLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다.");
        }
        mapLetterRepository.delete(letterId);
    }

    public List<FindMapLetter> findSentMapLetters(Long userId) {
        List<MapLetter> mapLetters=mapLetterRepository.findAllByCreateUserId(userId);

        return mapLetters.stream()
                .map(MapLetter::toFindSentMapLetter)
                .toList();
    }

    public List<FindMapLetter> findReceivedMapLetters(Long userId) {
        List<MapLetter> mapLetters=mapLetterRepository.findAllByTargetUserId(userId);

        return mapLetters.stream()
                .map(MapLetter::toFindSentMapLetter)
                .toList();
    }
}

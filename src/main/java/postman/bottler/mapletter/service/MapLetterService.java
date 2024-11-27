package postman.bottler.mapletter.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.DeleteArchivedLettersRequestDTO;
import postman.bottler.mapletter.dto.response.*;
import postman.bottler.mapletter.exception.MapLetterAlreadyDeletedException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;
    private final MapLetterArchiveRepository mapLetterArchiveRepository;
//    private final UserService userService;

    public MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId) {
        MapLetter mapLetter = MapLetter.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    public MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO, Long userId) {
        MapLetter mapLetter = MapLetter.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    public OneLetterResponseDTO findOneMepLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if (mapLetter.getType() == MapLetterType.PRIVATE && (!mapLetter.getTargetUserId().equals(userId) && !mapLetter.getCreateUserId().equals(userId))) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        if(mapLetter.isDeleted()){
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }

        OneLetterResponseDTO oneLetterResponseDTO = MapLetter.toOneLetterResponse(mapLetter);
        String profileImg = ""; //user 서비스 메서드 불러서 받기
        return OneLetterResponseDTO.builder()
                .title(oneLetterResponseDTO.title())
                .content(oneLetterResponseDTO.content())
                .profileImg(profileImg)
                .font(oneLetterResponseDTO.font())
                .paper(oneLetterResponseDTO.paper())
                .label(oneLetterResponseDTO.label())
                .createdAt(oneLetterResponseDTO.createdAt())
                .description(oneLetterResponseDTO.description())
                .build();
    }

    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        for(Long letterId : letters) {
            MapLetter findMapLetter = mapLetterRepository.findById(letterId);
            if (!findMapLetter.getCreateUserId().equals(userId)) {
                throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
            }
            mapLetterRepository.softDelete(letterId);
        }
    }

    public List<FindMapLetterResponseDTO> findSentMapLetters(Long userId) {
        List<MapLetter> mapLetters=mapLetterRepository.findActiveByCreateUserId(userId);

        return mapLetters.stream()
                .map(this::toFindSentMapLetter)
                .toList();
    }

    private FindMapLetterResponseDTO toFindSentMapLetter(MapLetter mapLetter) {
        String targetUserNickname="";
        if(mapLetter.getType()==MapLetterType.PRIVATE){
//            targetUserNickname=userService.getNicknameById(mapLetter.getTargetUserId()); //나중에 유저 서비스에서 받기
        }

        return FindMapLetterResponseDTO.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .description(mapLetter.getDescription())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .targetUserNickname(targetUserNickname)
                .build();
    }

    public List<FindReceivedMapLetterResponseDTO> findReceivedMapLetters(Long userId) {
        List<FindReceivedMapLetterResponseDTO> result = new ArrayList<>();

        List<MapLetter> targetMapLetters=mapLetterRepository.findActiveByTargetUserId(userId);
        targetMapLetterToFindReceivedMapLetterResponseDTO(targetMapLetters,result);

        List<ReplyMapLetter> replyMapLetters=replyMapLetterRepository.findActiveReplyMapLettersBySourceUserId(userId);
        replyMapLetterToFindReceivedMapLetterResponseDTO(replyMapLetters, result);

        result.sort(Comparator.comparing(FindReceivedMapLetterResponseDTO::createdAt).reversed());

        return result;
    }

    private List<FindReceivedMapLetterResponseDTO> targetMapLetterToFindReceivedMapLetterResponseDTO
            (List<MapLetter> mapLetters, List<FindReceivedMapLetterResponseDTO> result) {
        for(MapLetter mapLetter : mapLetters) {
            String senderNickname = "";
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
//            senderNickname=userService.getNicknameById(mapLetter.getTargetUserId()); //나중에 유저 서비스에서 받기
            }

            FindReceivedMapLetterResponseDTO dto = FindReceivedMapLetterResponseDTO.builder()
                    .letterId(mapLetter.getId())
                    .title(mapLetter.getTitle())
                    .description(mapLetter.getDescription())
                    .label(mapLetter.getLabel())
                    .createdAt(mapLetter.getCreatedAt())
                    .senderNickname(senderNickname)
                    .type("target")
                    .build();
            result.add(dto);
        }
        return result;
    }

    private List<FindReceivedMapLetterResponseDTO> replyMapLetterToFindReceivedMapLetterResponseDTO
            (List<ReplyMapLetter> mapLetters, List<FindReceivedMapLetterResponseDTO> result) {
        for(ReplyMapLetter mapLetter : mapLetters) {
            FindReceivedMapLetterResponseDTO dto = FindReceivedMapLetterResponseDTO.builder()
                    .letterId(mapLetter.getReplyLetterId())
                    .label(mapLetter.getLabel())
                    .createdAt(mapLetter.getCreatedAt())
                    .type("reply")
                    .sourceLetterId(mapLetter.getSourceLetterId())
                    .build();
            result.add(dto);
        }
        return result;
    }

    public List<FindNearbyLettersResponseDTO> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude, Long userId) {
        List<MapLetterAndDistance> letters = mapLetterRepository.findLettersByUserLocation(latitude, longitude, userId);

        return letters.stream()
                .map(letterWithDistance -> FindNearbyLettersResponseDTO.builder()
                        .letterId(letterWithDistance.getLetterId())
                        .latitude(letterWithDistance.getLatitude())
                        .longitude(letterWithDistance.getLongitude())
                        .title(letterWithDistance.getTitle())
                        .createdAt(letterWithDistance.getCreatedAt())
                        .distance(letterWithDistance.getDistance())
                        .target(letterWithDistance.getTargetUserId())
                        .createUserId(letterWithDistance.getCreateUserId())
                        .label(letterWithDistance.getLabel())
                        .description(letterWithDistance.getDescription())
                        .build()
                )
                .toList();
    }

    public ReplyMapLetter createReplyMapLetter(@Valid CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO, Long userId) {
        mapLetterRepository.findSourceMapLetterById(createReplyMapLetterRequestDTO.sourceLetter());
        ReplyMapLetter replyMapLetter = ReplyMapLetter.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        return replyMapLetterRepository.save(replyMapLetter);
    }

    public List<FindAllReplyMapLettersResponseDTO> findAllReplyMapLetter(Long letterId, Long userId) {
        MapLetter sourceLetter = mapLetterRepository.findById(letterId);
        if (!sourceLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        if(sourceLetter.isDeleted()){
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }

        List<ReplyMapLetter> findReply = replyMapLetterRepository.findReplyMapLettersBySourceLetterId(letterId);

        return findReply.stream()
                .map(ReplyMapLetter::toFindAllReplyMapLettersResponseDTO)
                .toList();
    }

    public OneReplyLetterResponseDTO findOneReplyMapLetter(Long letterId, Long userId) {
        ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
        MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
        if(replyMapLetter.isDeleted()){
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }else if(!replyMapLetter.getCreateUserId().equals(userId)&&!sourceLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        return OneReplyLetterResponseDTO.builder()
                .content(replyMapLetter.getContent())
                .sourceLetterId(replyMapLetter.getSourceLetterId())
                .font(replyMapLetter.getFont())
                .label(replyMapLetter.getLabel())
                .paper(replyMapLetter.getPaper())
                .createdAt(replyMapLetter.getCreatedAt())
                .build();
    }

    public MapLetterArchive mapLetterArchive(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if(mapLetter.isDeleted()){
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }else if(mapLetter.getType()==MapLetterType.PRIVATE) {
            throw new CommonForbiddenException("편지를 저장할 수 있는 권한이 없습니다.");
        }
        MapLetterArchive archive = MapLetterArchive.builder()
                .mapLetterId(letterId)
                .userId(userId)
                .build();

        return mapLetterArchiveRepository.save(archive);
    }

    public List<FindAllArchiveLetters> findArchiveLetters(Long userId) {
        return mapLetterArchiveRepository.findAllById(userId);
    }

    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO, Long userId) {
        for(Long archiveId : deleteArchivedLettersRequestDTO.archiveIds()) {
            MapLetterArchive findArchiveInfo = mapLetterArchiveRepository.findById(archiveId);
            if (!findArchiveInfo.getUserId().equals(userId)) {
                throw new CommonForbiddenException("편지 보관 취소를 할 권한이 없습니다. 편지 보관 취소에 실패했습니다.");
            }
            mapLetterArchiveRepository.deleteById(archiveId);
        }
    }
}

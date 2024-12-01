package postman.bottler.mapletter.service;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.DeleteArchivedLettersRequestDTO;
import postman.bottler.mapletter.dto.response.*;
import postman.bottler.mapletter.exception.DistanceToFarException;
import postman.bottler.mapletter.exception.LetterAlreadyReplyException;
import postman.bottler.mapletter.exception.MapLetterAlreadyArchivedException;
import postman.bottler.mapletter.exception.MapLetterAlreadyDeletedException;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;
    private final MapLetterArchiveRepository mapLetterArchiveRepository;
//    private final UserService userService;

    @Transactional
    public MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
                                           Long userId) {
        MapLetter mapLetter = MapLetter.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    @Transactional
    public MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO,
                                           Long userId) {
        MapLetter mapLetter = MapLetter.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    @Transactional(readOnly = true)
    public OneLetterResponseDTO findOneMapLetter(Long letterId, Long userId, BigDecimal latitude,
                                                 BigDecimal longitude) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if (mapLetter.getType() == MapLetterType.PRIVATE && (!mapLetter.getTargetUserId().equals(userId)
                && !mapLetter.getCreateUserId().equals(userId))) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        if (mapLetter.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }
        Double distance = mapLetterRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);
        if (distance > 15) {
            throw new DistanceToFarException("편지와의 거리가 멀어서 조회가 불가능합니다.");
        }

        String profileImg = ""; //user 서비스 메서드 불러서 받기
        return OneLetterResponseDTO.from(mapLetter, profileImg); //from으로 수정하기
    }

    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        for (Long letterId : letters) {
            MapLetter findMapLetter = mapLetterRepository.findById(letterId);
            if (!findMapLetter.getCreateUserId().equals(userId)) {
                throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
            }
            mapLetterRepository.softDelete(letterId);
        }
    }

    @Transactional(readOnly = true)
    public Page<FindMapLetterResponseDTO> findSentMapLetters(int page, int size, Long userId) {

//        Page<MapLetter> mapLetters = mapLetterRepository.findActiveByCreateUserId(userId,
//                PageRequest.of(page - 1, size, Sort.by(sort).descending()));

        Page<FindSentMapLetter> sentMapLetters = mapLetterRepository.findSentLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        return sentMapLetters.map(this::toFindSentMapLetter);
    }

    private FindMapLetterResponseDTO toFindSentMapLetter(FindSentMapLetter findSentMapLetter) {
        String targetUserNickname = null;
        if (findSentMapLetter.getType().equals("TARGET")) {
            targetUserNickname = "TS"; //추후 변경. 테스트용
//            targetUserNickname=userService.getNicknameById(mapLetter.getTargetUserId()); //나중에 유저 서비스에서 받기
        }

        return FindMapLetterResponseDTO.from(findSentMapLetter, targetUserNickname);
    }

    @Transactional(readOnly = true)
    public List<FindReceivedMapLetterResponseDTO> findReceivedMapLetters(Long userId) {
        List<FindReceivedMapLetterResponseDTO> result = new ArrayList<>();

        List<MapLetter> targetMapLetters = mapLetterRepository.findActiveByTargetUserId(userId);
        for (MapLetter mapLetter : targetMapLetters) {
            String senderNickname = "";
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
                // senderNickname = userService.getNicknameById(mapLetter.getTargetUserId());
            }
            result.add(FindReceivedMapLetterResponseDTO.fromTargetMapLetter(mapLetter, senderNickname));
        }

        List<ReplyMapLetter> replyMapLetters = replyMapLetterRepository.findActiveReplyMapLettersBySourceUserId(userId);
        for (ReplyMapLetter mapLetter : replyMapLetters) {
            result.add(FindReceivedMapLetterResponseDTO.fromReplyMapLetter(mapLetter));
        }

        result.sort(Comparator.comparing(FindReceivedMapLetterResponseDTO::createdAt).reversed());
        return result;
    }

    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponseDTO> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude,
                                                                   Long userId) {
        List<MapLetterAndDistance> letters = mapLetterRepository.findLettersByUserLocation(latitude, longitude, userId);

        return letters.stream().map(FindNearbyLettersResponseDTO::from).toList();
    }

    @Transactional
    public ReplyMapLetter createReplyMapLetter(@Valid CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
                                               Long userId) {
        boolean isReplied = replyMapLetterRepository.findByLetterIdAndUserId(
                createReplyMapLetterRequestDTO.sourceLetter(), userId);
        if (isReplied) {
            throw new LetterAlreadyReplyException("해당 편지에 이미 답장을 했습니다.");
        }

        mapLetterRepository.findSourceMapLetterById(createReplyMapLetterRequestDTO.sourceLetter());
        ReplyMapLetter replyMapLetter = ReplyMapLetter.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        return replyMapLetterRepository.save(replyMapLetter);
    }

    @Transactional(readOnly = true)
    public Page<FindAllReplyMapLettersResponseDTO> findAllReplyMapLetter(int page, int size, Long letterId,
                                                                         Long userId) {
        MapLetter sourceLetter = mapLetterRepository.findById(letterId);
        if (!sourceLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        if (sourceLetter.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }

        Page<ReplyMapLetter> findReply = replyMapLetterRepository.findReplyMapLettersBySourceLetterId(letterId,
                PageRequest.of(page - 1, size));

        return findReply.map(FindAllReplyMapLettersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public OneReplyLetterResponseDTO findOneReplyMapLetter(Long letterId, Long userId) {
        ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
        MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
        if (replyMapLetter.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        } else if (!replyMapLetter.getCreateUserId().equals(userId) && !sourceLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }

        return OneReplyLetterResponseDTO.from(replyMapLetter);
    }

    @Transactional
    public MapLetterArchive mapLetterArchive(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if (mapLetter.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        } else if (mapLetter.getType() == MapLetterType.PRIVATE) {
            throw new CommonForbiddenException("편지를 저장할 수 있는 권한이 없습니다.");
        }
        MapLetterArchive archive = MapLetterArchive.builder().mapLetterId(letterId).userId(userId).createdAt(
                LocalDateTime.now()).build();

        boolean isArchived = mapLetterArchiveRepository.findByLetterIdAndUserId(letterId, userId);
        if (isArchived) {
            throw new MapLetterAlreadyArchivedException("편지가 이미 저장되어 있습니다.");
        }

        return mapLetterArchiveRepository.save(archive);
    }

    @Transactional(readOnly = true)
    public Page<FindAllArchiveLetters> findArchiveLetters(int page, int size, Long userId) {
        return mapLetterArchiveRepository.findAllById(userId,
                PageRequest.of(page - 1, size));
    }

    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO, Long userId) {
        for (Long archiveId : deleteArchivedLettersRequestDTO.archiveIds()) {
            MapLetterArchive findArchiveInfo = mapLetterArchiveRepository.findById(archiveId);
            if (!findArchiveInfo.getUserId().equals(userId)) {
                throw new CommonForbiddenException("편지 보관 취소를 할 권한이 없습니다. 편지 보관 취소에 실패했습니다.");
            }
            mapLetterArchiveRepository.deleteById(archiveId);
        }
    }

    @Transactional(readOnly = true)
    public CheckReplyMapLetterResponseDTO checkReplyMapLetter(Long letterId, Long userId) {
        return new CheckReplyMapLetterResponseDTO(replyMapLetterRepository.findByLetterIdAndUserId(letterId, userId));
    }

    @Transactional
    public void letterBlock(BlockMapLetterType type, Long letterId) {
        if (type == BlockMapLetterType.MAP_LETTER) {
            mapLetterRepository.letterBlock(letterId);
        } else if (type == BlockMapLetterType.REPLY) {
            replyMapLetterRepository.letterBlock(letterId);
        }
    }

    @Transactional
    public void deleteReplyMapLetter(List<Long> letters, Long userId) {
        for (Long letterId : letters) {
            ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
            if (!replyMapLetter.getCreateUserId().equals(userId)) {
                throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
            }
            replyMapLetterRepository.softDelete(letterId);
        }
    }
}

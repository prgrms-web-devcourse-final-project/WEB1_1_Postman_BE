package postman.bottler.mapletter.service;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.DeleteArchivedLettersRequestDTO;
import postman.bottler.mapletter.dto.response.*;
import postman.bottler.mapletter.exception.BlockedLetterException;
import postman.bottler.mapletter.exception.DistanceToFarException;
import postman.bottler.mapletter.exception.LetterAlreadyReplyException;
import postman.bottler.mapletter.exception.MapLetterAlreadyArchivedException;
import postman.bottler.mapletter.exception.MapLetterAlreadyDeletedException;

import java.math.BigDecimal;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;
    private final MapLetterArchiveRepository mapLetterArchiveRepository;
//    private final UserService userService;

    private static final int VIEW_DISTANCE = 15;

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
        if (mapLetter.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }

        Double distance = mapLetterRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);
        if (distance > VIEW_DISTANCE) {
            throw new DistanceToFarException("편지와의 거리가 멀어서 조회가 불가능합니다.");
        }

        String profileImg = ""; //user 서비스 메서드 불러서 받기
        return OneLetterResponseDTO.from(mapLetter, profileImg);
    }

    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        for (Long letterId : letters) {
            MapLetter findMapLetter = mapLetterRepository.findById(letterId);
            if (!findMapLetter.getCreateUserId().equals(userId)) {
                throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
            }
            if (findMapLetter.isBlocked()) {
                throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
            }
            mapLetterRepository.softDelete(letterId);
        }
    }

    @Transactional(readOnly = true)
    public Page<FindMapLetterResponseDTO> findSentMapLetters(int page, int size, Long userId) {
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
    public Page<FindReceivedMapLetterResponseDTO> findReceivedMapLetters(int page, int size, Long userId) {
        Page<FindReceivedMapLetterDTO> letters = mapLetterRepository.findActiveReceivedMapLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        return letters.map(letter -> {
            String senderNickname = null;
            String senderProfileImg = null;

            if ("TARGET".equals(letter.getType())) {
                senderNickname = "TS"; // 예시 닉네임 (유저 서비스에서 가져와야 함)
                senderProfileImg = "www.profile.com"; // 예시 프로필 이미지
                // senderNickname = userService.getNicknameById(projection.getSenderId());
                // senderProfileImg = userService.getProfileImgById(projection.getSenderId());
            }

            return FindReceivedMapLetterResponseDTO.from(letter, senderNickname, senderProfileImg);
        });
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
        if (sourceLetter.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
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
        if (sourceLetter.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
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
        if (mapLetter.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }

        MapLetterArchive archive = MapLetterArchive.builder().mapLetterId(letterId).userId(userId)
                .createdAt(LocalDateTime.now()).build();

        boolean isArchived = mapLetterArchiveRepository.findByLetterIdAndUserId(letterId, userId);
        if (isArchived) {
            throw new MapLetterAlreadyArchivedException("편지가 이미 저장되어 있습니다.");
        }

        return mapLetterArchiveRepository.save(archive);
    }

    @Transactional(readOnly = true)
    public Page<FindAllArchiveLetters> findArchiveLetters(int page, int size, Long userId) {
        return mapLetterArchiveRepository.findAllById(userId, PageRequest.of(page - 1, size));
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
    public Long letterBlock(BlockMapLetterType type, Long letterId) { //userId return
        if (type == BlockMapLetterType.MAP_LETTER) {
            mapLetterRepository.letterBlock(letterId);
            return mapLetterRepository.findById(letterId).getCreateUserId();
        } else if (type == BlockMapLetterType.REPLY) {
            replyMapLetterRepository.letterBlock(letterId);
            return replyMapLetterRepository.findById(letterId).getCreateUserId();
        }
        return null;
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

    public OneLetterResponseDTO findArchiveOneLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        if (mapLetter.getType() == MapLetterType.PRIVATE && (!mapLetter.getTargetUserId().equals(userId)
                && !mapLetter.getCreateUserId().equals(userId))) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        if (mapLetter.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }
        if (mapLetter.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }

        String profileImg = "www.profile.com"; //user 서비스 메서드 불러서 받기
//        profileImg = userService.getProfileImgByCreateUserId(mapLetter.getCreateUserId());
        return OneLetterResponseDTO.from(mapLetter, profileImg);
    }

    public Page<FindAllSentReplyMapLetterResponseDTO> findAllSentReplyMapLetter(int page, int size, Long userId) {
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findAllSentReplyByUserId(userId,
                PageRequest.of(page - 1, size));

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();

            return FindAllSentReplyMapLetterResponseDTO.from(replyMapLetter, title);
        });
    }

    public Page<FindAllSentMapLetterResponseDTO> findAllSentMapLetter(int page, int size, Long userId) {
        Page<MapLetter> letters = mapLetterRepository.findActiveByCreateUserId(userId, PageRequest.of(page - 1, size));

        return letters.map(mapLetter -> {
            String targetUserNickname = null;
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
//                targetUserNickname=userService.findNicknameByUserId(mapLetter.getTargetUserId());
                targetUserNickname = "TS";
            }
            return FindAllSentMapLetterResponseDTO.from(mapLetter, targetUserNickname);
        });
    }

    public Page<FindAllReceivedReplyLetterResponseDTO> findAllReceivedReplyLetter(int page, int size, Long userId) {
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findActiveReplyMapLettersBySourceUserId(userId,
                PageRequest.of(page - 1, size));

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();
            return FindAllReceivedReplyLetterResponseDTO.from(replyMapLetter, title);
        });
    }

    public Page<FindAllReceivedLetterResponseDTO> findAllReceivedLetter(int page, int size, Long userId) {
        Page<MapLetter> letters = mapLetterRepository.findActiveByTargetUserId(userId, PageRequest.of(page - 1, size));
        return letters.map(letter -> {
//            String sendUserNickname = letter.getCreateUserId().toString(); //예시
            String sendUserNickname = "TS";
            String sendUserProfileImg = "www.profile.com"; //예시

//             sendUserNickname = userService.getNicknameById(letter.getCreateUserId());
//             sendUserProfileImg = userService.getProfileImgById(letter.getCreateUserId());
            return FindAllReceivedLetterResponseDTO.from(letter, sendUserNickname, sendUserProfileImg);
        });
    }
}

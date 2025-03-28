package postman.bottler.mapletter.application.service;

import static postman.bottler.notification.domain.NotificationType.MAP_REPLY;
import static postman.bottler.notification.domain.NotificationType.TARGET_LETTER;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.mapletter.application.BlockMapLetterType;
import postman.bottler.mapletter.application.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.application.dto.FindSentMapLetter;
import postman.bottler.mapletter.application.dto.MapLetterAndDistance;
import postman.bottler.mapletter.application.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteArchivedLettersRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteArchivedLettersRequestDTOV1;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO.LetterInfo;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO.LetterType;
import postman.bottler.mapletter.application.dto.response.CheckReplyMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.application.dto.response.FindAllReceivedLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllReceivedReplyLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllReplyMapLettersResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllSentMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllSentReplyMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindNearbyLettersResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindReceivedMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.OneLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.OneReplyLetterResponseDTO;
import postman.bottler.mapletter.application.repository.MapLetterArchiveRepository;
import postman.bottler.mapletter.application.repository.MapLetterRepository;
import postman.bottler.mapletter.application.repository.RecentReplyStorage;
import postman.bottler.mapletter.application.repository.ReplyMapLetterRepository;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterArchive;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.exception.LetterAlreadyReplyException;
import postman.bottler.mapletter.exception.MapLetterAlreadyArchivedException;
import postman.bottler.mapletter.exception.MapLetterNotFoundException;
import postman.bottler.mapletter.exception.PageRequestException;
import postman.bottler.mapletter.exception.TypeNotFoundException;
import postman.bottler.notification.application.dto.request.NotificationLabelRequestDTO;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.reply.application.dto.ReplyType;
import postman.bottler.user.application.service.UserService;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;
    private final MapLetterArchiveRepository mapLetterArchiveRepository;
    private final UserService userService;
    private final NotificationService notificationService;
    private final RecentReplyStorage recentReplyStorage;

    private static final double VIEW_DISTANCE = 15;

    @Transactional
    public MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
                                           Long userId) {
        MapLetter mapLetter = MapLetter.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return mapLetterRepository.save(mapLetter);
    }

    @Transactional
    public MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO,
                                           Long userId) {
        Long targetUserId = userService.getUserIdByNickname(createTargetMapLetterRequestDTO.target());
        MapLetter mapLetter = MapLetter.createTargetMapLetter(createTargetMapLetterRequestDTO, userId, targetUserId);
        MapLetter save = mapLetterRepository.save(mapLetter);
        notificationService.sendLetterNotification(TARGET_LETTER, targetUserId, save.getId(), save.getLabel());
        return save;
    }

    @Transactional
    public OneLetterResponseDTO findOneMapLetter(Long letterId, Long userId, BigDecimal latitude,
                                                 BigDecimal longitude) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);

        Double distance = mapLetterRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);
        mapLetter.validateAccess(userId);

        if (mapLetter.isTargetUser(userId)) {
            mapLetterRepository.updateRead(mapLetter);
        }

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponseDTO.from(mapLetter, profileImg, mapLetter.getCreateUserId() == userId,
                checkReplyMapLetter(letterId, userId).isReplied(), isArchived(letterId, userId));
    }

    @Transactional
    public void deleteMapLetter(List<Long> letters, Long userId) {
        for (Long letterId : letters) {
            MapLetter findMapLetter = mapLetterRepository.findById(letterId);
            findMapLetter.validDeleteMapLetter(userId);
            mapLetterRepository.softDelete(letterId);
        }
    }

    @Transactional(readOnly = true)
    public Page<FindMapLetterResponseDTO> findSentMapLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindSentMapLetter> sentMapLetters = mapLetterRepository.findSentLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        if (sentMapLetters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(sentMapLetters.getTotalPages(), page);

        return sentMapLetters.map(this::toFindSentMapLetter);
    }

    private FindMapLetterResponseDTO toFindSentMapLetter(FindSentMapLetter findSentMapLetter) {
        String targetUserNickname = null;
        if (findSentMapLetter.getType().equals("TARGET")) {
            targetUserNickname = userService.getNicknameById(findSentMapLetter.getTargetUser());
        }

        return FindMapLetterResponseDTO.from(findSentMapLetter, targetUserNickname,
                findSentMapLetter.getType().equals("REPLY") ? LetterType.REPLY : LetterType.MAP);
    }

    @Transactional(readOnly = true)
    public Page<FindReceivedMapLetterResponseDTO> findReceivedMapLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindReceivedMapLetterDTO> letters = mapLetterRepository.findActiveReceivedMapLettersByUserId(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);

        return letters.map(letter -> {
            String senderNickname = null;
            String senderProfileImg = null;

            if ("TARGET".equals(letter.getType())) {
                senderNickname = userService.getNicknameById(letter.getSenderId());
                senderProfileImg = userService.getProfileImageUrlById(letter.getSenderId());
            }

            return FindReceivedMapLetterResponseDTO.from(letter, senderNickname, senderProfileImg, LetterType.MAP);
        });
    }

    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponseDTO> findNearByMapLetters(BigDecimal latitude, BigDecimal longitude,
                                                                   Long userId) {
        List<MapLetterAndDistance> letters = mapLetterRepository.findLettersByUserLocation(latitude, longitude, userId);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponseDTO.from(letter, nickname);
                        }
                ).toList();
    }

    @Transactional
    public ReplyMapLetter createReplyMapLetter(@Valid CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
                                               Long userId) {
        boolean isReplied = replyMapLetterRepository.findByLetterIdAndUserId(
                createReplyMapLetterRequestDTO.sourceLetter(), userId);
        if (isReplied) {
            throw new LetterAlreadyReplyException("해당 편지에 이미 답장을 했습니다.");
        }

        MapLetter source = mapLetterRepository.findSourceMapLetterById(createReplyMapLetterRequestDTO.sourceLetter());
        ReplyMapLetter replyMapLetter = ReplyMapLetter.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        ReplyMapLetter save = replyMapLetterRepository.save(replyMapLetter);

        MapLetter sourceLetter = mapLetterRepository.findById(save.getSourceLetterId());
        recentReplyStorage.saveRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                save.getReplyLetterId(), save.getLabel());

        notificationService.sendLetterNotification(MAP_REPLY, source.getCreateUserId(), save.getReplyLetterId(),
                save.getLabel());

        return save;
    }

    @Transactional(readOnly = true)
    public Page<FindAllReplyMapLettersResponseDTO> findAllReplyMapLetter(int page, int size, Long letterId,
                                                                         Long userId) {
        validMinPage(page);
        MapLetter sourceLetter = mapLetterRepository.findById(letterId);

        sourceLetter.validFindAllReplyMapLetter(userId);

        Page<ReplyMapLetter> findReply = replyMapLetterRepository.findReplyMapLettersBySourceLetterId(letterId,
                PageRequest.of(page - 1, size));

        if (findReply.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(findReply.getTotalPages(), page);

        return findReply.map(FindAllReplyMapLettersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public OneReplyLetterResponseDTO findOneReplyMapLetter(Long letterId, Long userId) {
        ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
        MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());

        replyMapLetter.validFindOneReplyMapLetter(userId, sourceLetter);
        sourceLetter.validDeleteAndBlocked();

        return OneReplyLetterResponseDTO.from(replyMapLetter, userId == replyMapLetter.getCreateUserId());
    }

    @Transactional
    public MapLetterArchive mapLetterArchive(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        mapLetter.validMapLetterArchive();

        MapLetterArchive archive = MapLetterArchive.builder().mapLetterId(letterId).userId(userId)
                .createdAt(LocalDateTime.now()).build();

        if (isArchived(letterId, userId)) {
            throw new MapLetterAlreadyArchivedException("편지가 이미 저장되어 있습니다.");
        }

        return mapLetterArchiveRepository.save(archive);
    }

    @Transactional(readOnly = true)
    public Page<FindAllArchiveLetters> findArchiveLetters(int page, int size, Long userId) {
        validMinPage(page);
        Page<FindAllArchiveLetters> letters = mapLetterArchiveRepository.findAllById(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);
        return letters;
    }

    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO, Long userId) {
        List<MapLetterArchive> archiveInfos = mapLetterArchiveRepository.findAllById(
                deleteArchivedLettersRequestDTO.letterIds());

        if (archiveInfos.size() != deleteArchivedLettersRequestDTO.letterIds().size()) {
            throw new MapLetterNotFoundException("편지를 찾을 수 없습니다.");
        } //일부 아이디가 존재하지 않을 경우 예외 처리

        for (MapLetterArchive archiveInfo : archiveInfos) {
            archiveInfo.validDeleteArchivedLetter(userId);
        }

        mapLetterArchiveRepository.deleteAllByIdInBatch(deleteArchivedLettersRequestDTO.letterIds());
    }

    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersRequestDTOV1 deleteArchivedLettersRequestDTO,
                                     Long userId) { //삭제 예정
        for (Long archiveId : deleteArchivedLettersRequestDTO.archiveIds()) {
            MapLetterArchive findArchiveInfo = mapLetterArchiveRepository.findById(archiveId);
            findArchiveInfo.validDeleteArchivedLetter(userId);
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
        List<ReplyMapLetter> replyMapLetters = new ArrayList<>();

        for (Long letterId : letters) {
            ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
            replyMapLetter.validDeleteReplyMapLetter(userId);
            replyMapLetterRepository.softDelete(letterId);

            replyMapLetters.add(replyMapLetter);
        }
        deleteRecentRepliesFromRedis(replyMapLetters);
    }

    private void deleteRecentRepliesFromRedis(List<ReplyMapLetter> replyMapLetters) {
        for (ReplyMapLetter replyMapLetter : replyMapLetters) {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            recentReplyStorage.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                    replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
        }
    }

    @Transactional(readOnly = true)
    public OneLetterResponseDTO findArchiveOneLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        mapLetter.validateAccess(userId);

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponseDTO.from(mapLetter, profileImg, mapLetter.getCreateUserId() == userId,
                checkReplyMapLetter(letterId, userId).isReplied(), isArchived(letterId, userId));
    }

    public Page<FindAllSentReplyMapLetterResponseDTO> findAllSentReplyMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findAllSentReplyByUserId(userId,
                PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();

            return FindAllSentReplyMapLetterResponseDTO.from(replyMapLetter, title, LetterType.REPLY);
        });
    }

    public Page<FindAllSentMapLetterResponseDTO> findAllSentMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterRepository.findActiveByCreateUserId(userId, PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(mapLetter -> {
            String targetUserNickname = null;
            if (mapLetter.getType() == MapLetterType.PRIVATE) {
                targetUserNickname = userService.getNicknameById(mapLetter.getTargetUserId());
            }
            return FindAllSentMapLetterResponseDTO.from(mapLetter, targetUserNickname, LetterType.MAP);
        });
    }

    public Page<FindAllReceivedReplyLetterResponseDTO> findAllReceivedReplyLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findActiveReplyMapLettersBySourceUserId(userId,
                PageRequest.of(page - 1, size));

        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }

        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();
            return FindAllReceivedReplyLetterResponseDTO.from(replyMapLetter, title, LetterType.MAP);
        });
    }

    public Page<FindAllReceivedLetterResponseDTO> findAllReceivedLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterRepository.findActiveByTargetUserId(userId, PageRequest.of(page - 1, size));
        if (letters.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), PageRequest.of(page - 1, size), 0);
        }
        validMaxPage(letters.getTotalPages(), page);
        return letters.map(letter -> {
            String sendUserNickname = userService.getNicknameById(letter.getCreateUserId());
            String sendUserProfileImg = userService.getProfileImageUrlById(letter.getCreateUserId());
            return FindAllReceivedLetterResponseDTO.from(letter, sendUserNickname, sendUserProfileImg, LetterType.MAP);
        });
    }

    private void validMaxPage(int maxPage, int nowPage) {
        if (maxPage < nowPage) {
            throw new PageRequestException("페이지가 존재하지 않습니다.");
        }
    }

    private void validMinPage(int nowPage) {
        if (nowPage < 1) {
            throw new PageRequestException("페이지가 존재하지 않습니다.");
        }
    }

    @Transactional(readOnly = true)
    public List<FindNearbyLettersResponseDTO> guestFindNearByMapLetters(BigDecimal latitude, BigDecimal longitude) {
        List<MapLetterAndDistance> letters = mapLetterRepository.guestFindLettersByUserLocation(latitude, longitude);

        return letters.stream()
                .map(letter -> {
                            String nickname = userService.getNicknameById(letter.getCreateUserId());
                            return FindNearbyLettersResponseDTO.from(letter, nickname);
                        }
                ).toList();
    }

    @Transactional(readOnly = true)
    public OneLetterResponseDTO guestFindOneMapLetter(Long letterId, BigDecimal latitude, BigDecimal longitude) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);

        Double distance = mapLetterRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);

        mapLetter.isPrivate();

        mapLetter.validateFindOneMapLetter(VIEW_DISTANCE, distance);

        String profileImg = userService.getProfileImageUrlById(mapLetter.getCreateUserId());
        return OneLetterResponseDTO.from(mapLetter, profileImg, false, false, false);
    }

    @Transactional(readOnly = true)
    public List<NotificationLabelRequestDTO> getLabels(List<Long> ids) {
        List<MapLetter> finds = mapLetterRepository.findAllByIds(ids);
        return finds.stream()
                .map(find -> new NotificationLabelRequestDTO(find.getId(), find.getLabel()))
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isArchived(Long letterId, Long userId) {
        return mapLetterArchiveRepository.findByLetterIdAndUserId(letterId, userId);
    }

    @Transactional
    public void deleteAllMapLetters(String type, Long userId) {
        switch (type) {
            case "SENT":
                mapLetterRepository.softDeleteAllByCreateUserId(userId);
                replyMapLetterRepository.softDeleteAllByCreateUserId(userId);
                break;
            case "SENT-MAP":
                mapLetterRepository.softDeleteAllByCreateUserId(userId);
                break;
            case "SENT-REPLY":
                replyMapLetterRepository.softDeleteAllByCreateUserId(userId);
                break;
            case "RECEIVED":
                mapLetterRepository.softDeleteAllForRecipient(userId);
                replyMapLetterRepository.softDeleteAllForRecipient(userId);
                break;
            case "RECEIVED-MAP":
                mapLetterRepository.softDeleteAllForRecipient(userId);
                break;
            case "RECEIVED-REPLY":
                replyMapLetterRepository.softDeleteAllForRecipient(userId);
                break;
            default:
                throw new TypeNotFoundException("잘못된 편지 타입입니다.");
        }
    }

    @Transactional
    public void deleteSentMapLetters(DeleteMapLettersRequestDTO letters, Long userId) {
        for (LetterInfo letter : letters.letters()) {
            switch (letter.letterType()) {
                case MAP:
                    MapLetter findMapLetter = mapLetterRepository.findById(letter.letterId());
                    findMapLetter.validDeleteMapLetter(userId);
                    mapLetterRepository.softDelete(letter.letterId());
                    break;
                case REPLY:
                    ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letter.letterId());
                    replyMapLetter.validDeleteReplyMapLetter(userId);
                    replyMapLetterRepository.softDelete(letter.letterId());

                    MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
                    recentReplyStorage.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                            replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
                    break;
                default:
                    throw new TypeNotFoundException("잘못된 편지 타입입니다.");
            }
        }
    }

    @Transactional
    public void deleteReceivedMapLetters(DeleteMapLettersRequestDTO letters, Long userId) {
        for (LetterInfo letter : letters.letters()) {
            switch (letter.letterType()) {
                case MAP:
                    MapLetter findMapLetter = mapLetterRepository.findById(letter.letterId());
                    findMapLetter.validateRecipientDeletion(userId);
                    mapLetterRepository.softDeleteForRecipient(letter.letterId());
                    break;
                case REPLY:
                    ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letter.letterId());
                    MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
                    replyMapLetter.validateRecipientDeletion(userId, sourceLetter.getCreateUserId());
                    replyMapLetterRepository.softDeleteForRecipient(letter.letterId());

                    recentReplyStorage.deleteRecentReply(sourceLetter.getCreateUserId(), ReplyType.MAP.name(),
                            replyMapLetter.getReplyLetterId(), replyMapLetter.getLabel());
                    break;
                default:
                    throw new TypeNotFoundException("잘못된 편지 타입입니다.");
            }
        }
    }
}

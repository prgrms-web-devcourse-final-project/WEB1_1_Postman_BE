package postman.bottler.mapletter.service;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import postman.bottler.mapletter.dto.response.CheckReplyMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.dto.response.FindAllReceivedLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindAllReceivedReplyLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindAllReplyMapLettersResponseDTO;
import postman.bottler.mapletter.dto.response.FindAllSentMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindAllSentReplyMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindNearbyLettersResponseDTO;
import postman.bottler.mapletter.dto.response.FindReceivedMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.OneLetterResponseDTO;
import postman.bottler.mapletter.dto.response.OneReplyLetterResponseDTO;
import postman.bottler.mapletter.exception.LetterAlreadyReplyException;
import postman.bottler.mapletter.exception.MapLetterAlreadyArchivedException;
import postman.bottler.mapletter.exception.PageRequestException;
import postman.bottler.reply.dto.ReplyType;

@Service
@RequiredArgsConstructor
public class MapLetterService {
    private final MapLetterRepository mapLetterRepository;
    private final ReplyMapLetterRepository replyMapLetterRepository;
    private final MapLetterArchiveRepository mapLetterArchiveRepository;
    //    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    private static final double VIEW_DISTANCE = 15;
    private static final int REDIS_SAVED_REPLY = 6;

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

        Double distance = mapLetterRepository.findDistanceByLatitudeAndLongitudeAndLetterId(latitude, longitude,
                letterId);

        mapLetter.validateFindOneMapLetter(userId, VIEW_DISTANCE, distance);

        String profileImg = ""; //user 서비스 메서드 불러서 받기
        return OneLetterResponseDTO.from(mapLetter, profileImg);
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
        validMaxPage(sentMapLetters.getTotalPages(), page);

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
        validMinPage(page);
        Page<FindReceivedMapLetterDTO> letters = mapLetterRepository.findActiveReceivedMapLettersByUserId(userId,
                PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);

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
        ReplyMapLetter save = replyMapLetterRepository.save(replyMapLetter);
        saveRecentReply(save.getReplyLetterId(), save.getLabel(), save.getSourceLetterId());
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

        validMaxPage(findReply.getTotalPages(), page);

        return findReply.map(FindAllReplyMapLettersResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public OneReplyLetterResponseDTO findOneReplyMapLetter(Long letterId, Long userId) {
        ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
        MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());

        replyMapLetter.validFindOneReplyMapLetter(userId, sourceLetter);
        sourceLetter.validDeleteAndBlocked();

        return OneReplyLetterResponseDTO.from(replyMapLetter);
    }

    @Transactional
    public MapLetterArchive mapLetterArchive(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        mapLetter.validMapLetterArchive();

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
        validMinPage(page);
        Page<FindAllArchiveLetters> letters = mapLetterArchiveRepository.findAllById(userId,
                PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);
        return letters;
    }

    @Transactional
    public void deleteArchivedLetter(DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO, Long userId) {
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
        for (Long letterId : letters) {
            ReplyMapLetter replyMapLetter = replyMapLetterRepository.findById(letterId);
            replyMapLetter.validDeleteReplyMapLetter(userId);
            replyMapLetterRepository.softDelete(letterId);
        }
    }

    public OneLetterResponseDTO findArchiveOneLetter(Long letterId, Long userId) {
        MapLetter mapLetter = mapLetterRepository.findById(letterId);
        mapLetter.validateAccess(userId);

        String profileImg = "www.profile.com"; //user 서비스 메서드 불러서 받기
//        profileImg = userService.getProfileImgByCreateUserId(mapLetter.getCreateUserId());
        return OneLetterResponseDTO.from(mapLetter, profileImg);
    }

    public Page<FindAllSentReplyMapLetterResponseDTO> findAllSentReplyMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findAllSentReplyByUserId(userId,
                PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();

            return FindAllSentReplyMapLetterResponseDTO.from(replyMapLetter, title);
        });
    }

    public Page<FindAllSentMapLetterResponseDTO> findAllSentMapLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterRepository.findActiveByCreateUserId(userId, PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);

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
        validMinPage(page);
        Page<ReplyMapLetter> letters = replyMapLetterRepository.findActiveReplyMapLettersBySourceUserId(userId,
                PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);

        return letters.map(replyMapLetter -> {
            MapLetter sourceLetter = mapLetterRepository.findById(replyMapLetter.getSourceLetterId());
            String title = "Re: " + sourceLetter.getTitle();
            return FindAllReceivedReplyLetterResponseDTO.from(replyMapLetter, title);
        });
    }

    public Page<FindAllReceivedLetterResponseDTO> findAllReceivedLetter(int page, int size, Long userId) {
        validMinPage(page);
        Page<MapLetter> letters = mapLetterRepository.findActiveByTargetUserId(userId, PageRequest.of(page - 1, size));
        validMaxPage(letters.getTotalPages(), page);
        return letters.map(letter -> {
//            String sendUserNickname = letter.getCreateUserId().toString(); //예시
            String sendUserNickname = "TS";
            String sendUserProfileImg = "www.profile.com"; //예시

//             sendUserNickname = userService.getNicknameById(letter.getCreateUserId());
//             sendUserProfileImg = userService.getProfileImgById(letter.getCreateUserId());
            return FindAllReceivedLetterResponseDTO.from(letter, sendUserNickname, sendUserProfileImg);
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

    private void saveRecentReply(Long letterId, String labelUrl, Long sourceLetterId) {
        MapLetter sourceLetter = mapLetterRepository.findById(sourceLetterId);
        String key = "REPLY:" + sourceLetter.getCreateUserId();
        String value = ReplyType.MAP+":"+letterId+":"+labelUrl;

        Long size = redisTemplate.opsForList().size(key);
        if (size != null && size >= REDIS_SAVED_REPLY) {
            redisTemplate.opsForList().rightPop(key);
        } else if (size == null || size <= 0) {
            Long itemsToFetch = REDIS_SAVED_REPLY - size;
            if (itemsToFetch > 0) {
                List<ReplyMapLetter> recentReplyLetters = replyMapLetterRepository.findRecentReplyByUserId(
                        sourceLetter.getCreateUserId(),
                        itemsToFetch);
                for (ReplyMapLetter replyMapLetter : recentReplyLetters) {
                    String tempValue = ReplyType.MAP+":"+replyMapLetter.getReplyLetterId()+":"+replyMapLetter.getLabel();
                    redisTemplate.opsForList().leftPush(key, tempValue);
                }
            }
        }

        if (!redisTemplate.opsForList().range(key, 0, -1).contains(value)) {
            redisTemplate.opsForList().leftPush(key, value);
        }
    }
}

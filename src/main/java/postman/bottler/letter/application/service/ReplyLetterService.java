package postman.bottler.letter.application.service;

import static postman.bottler.notification.domain.NotificationType.KEYWORD_REPLY;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.service.RedisLetterService;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.dto.ReceiverDTO;
import postman.bottler.letter.application.dto.request.PageRequestDTO;
import postman.bottler.letter.application.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterDetailResponseDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterSummaryResponseDTO;
import postman.bottler.letter.application.repository.ReplyLetterRepository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.exception.DuplicateReplyLetterException;
import postman.bottler.letter.exception.InvalidLetterRequestException;
import postman.bottler.letter.exception.LetterAuthorMismatchException;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.notification.application.service.NotificationService;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterService letterService;
    private final LetterBoxService letterBoxService;
    private final NotificationService notificationService;
    private final RedisLetterService redisLetterService;

    @Transactional
    public ReplyLetterResponseDTO createReplyLetter(Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId) {
        log.info("답장 생성 요청: senderId={}, letterId={}", senderId, letterId);

        if (checkIsReplied(letterId, senderId)) {
            throw new DuplicateReplyLetterException(letterId, senderId);
        }

        ReplyLetter replyLetter = saveReplyLetter(letterId, requestDTO, senderId);

        handleReplyPostProcessing(replyLetter, requestDTO.label());

        log.info("답장 생성 완료: replyLetterId={}, senderId={}, receiverId={}", replyLetter.getId(), senderId,
                replyLetter.getReceiverId());

        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterSummaryResponseDTO> findReplyLetterSummaries(Long letterId, PageRequestDTO pageRequestDTO,
                                                                        Long receiverId) {
        log.debug("답장 요약 정보 조회 요청: letterId={}, receiverId={}", letterId, receiverId);

        Page<ReplyLetterSummaryResponseDTO> summaries = replyLetterRepository.findAllByLetterIdAndReceiverId(letterId,
                receiverId, pageRequestDTO.toPageable()).map(ReplyLetterSummaryResponseDTO::from);

        log.info("답장 요약 정보 조회 완료: letterId={}, receiverId={}, count={}", letterId, receiverId,
                summaries.getTotalElements());

        return summaries;
    }

    @Transactional(readOnly = true)
    public ReplyLetterDetailResponseDTO findReplyLetterDetail(Long replyLetterId, Long userId) {
        log.debug("답장 상세 조회 요청: replyLetterId={}, userId={}", replyLetterId, userId);

        boolean isReplied = checkIsReplied(replyLetterId, userId);
        ReplyLetter replyLetter = findReplyLetter(replyLetterId);

        log.info("답장 상세 조회 완료: replyLetterId={}, userId={}, isReplied={}", replyLetterId, userId, isReplied);

        return ReplyLetterDetailResponseDTO.from(replyLetter, isReplied);
    }

    @Transactional
    public void softDeleteReplyLetters(List<Long> replyLetterIds, Long userId) {
        log.info("답장 삭제 요청: userId={}, replyLetterIds={}", userId, replyLetterIds);

        if (replyLetterIds == null || replyLetterIds.isEmpty()) {
            throw new InvalidLetterRequestException("삭제할 답장 편지 ID 목록이 비어 있습니다.");
        }

        List<ReplyLetter> replyLetters = replyLetterRepository.findAllByIds(replyLetterIds);

        if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.getSenderId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }

        replyLetters.forEach(
                replyLetter -> redisLetterService.deleteRecentReply(replyLetter.getReceiverId(), replyLetter.getId(),
                        replyLetter.getLabel()));

        replyLetterRepository.softDeleteByIds(replyLetterIds);

        log.info("답장 삭제 완료: userId={}, count={}", userId, replyLetterIds.size());
    }

    @Transactional
    public Long softBlockLetter(Long replyLetterId) {
        log.info("답장 차단 요청: replyLetterId={}", replyLetterId);

        ReplyLetter replyLetter = findReplyLetter(replyLetterId);
        replyLetterRepository.softBlockById(replyLetterId);

        log.info("답장 차단 완료: replyLetterId={}, senderId={}", replyLetterId, replyLetter.getSenderId());
        return replyLetter.getSenderId();
    }

    @Transactional
    public boolean checkIsReplied(Long letterId, Long senderId) {
        log.debug("답장 여부 확인: letterId={}, senderId={}", letterId, senderId);

        boolean exists = replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId);

        log.info("답장 여부 확인 결과: letterId={}, senderId={}, isReplied={}", letterId, senderId, exists);
        return exists;
    }

    private ReplyLetter saveReplyLetter(Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId) {
        log.debug("답장 저장 시작: letterId={}, senderId={}", letterId, senderId);

        ReceiverDTO receiverInfo = letterService.findReceiverInfo(letterId);
        String title = formatReplyTitle(receiverInfo.title());

        ReplyLetter savedReplyLetter = replyLetterRepository.save(
                requestDTO.toDomain(title, letterId, receiverInfo.receiverId(), senderId));

        log.info("답장 저장 완료: replyLetterId={}, senderId={}, receiverId={}", savedReplyLetter.getId(), senderId,
                receiverInfo.receiverId());

        return savedReplyLetter;
    }

    private String formatReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private void handleReplyPostProcessing(ReplyLetter replyLetter, String labelUrl) {
        log.debug("답장 생성 후처리 시작: replyLetterId={}", replyLetter.getId());

        saveReplyLetterToBox(replyLetter);
        redisLetterService.saveReplyToRedis(replyLetter.getLetterId(), labelUrl, replyLetter.getReceiverId());
        sendReplyNotification(replyLetter);

        log.info("답장 생성 후처리 완료: replyLetterId={}", replyLetter.getId());
    }

    private void saveReplyLetterToBox(ReplyLetter replyLetter) {
        saveLetterToBox(replyLetter.getSenderId(), replyLetter, BoxType.SEND);
        saveLetterToBox(replyLetter.getReceiverId(), replyLetter, BoxType.RECEIVE);
    }

    private void saveLetterToBox(Long userId, ReplyLetter replyLetter, BoxType boxType) {
        letterBoxService.saveLetter(LetterBoxDTO.of(userId, replyLetter.getId(), LetterType.REPLY_LETTER, boxType,
                replyLetter.getCreatedAt()));
    }

    private void sendReplyNotification(ReplyLetter replyLetter) {
        log.info("답장 알림 전송 요청: receiverId={}, replyLetterId={}", replyLetter.getReceiverId(), replyLetter.getId());

        notificationService.sendNotification(KEYWORD_REPLY, replyLetter.getReceiverId(), replyLetter.getId(),
                replyLetter.getLabel());

        log.info("답장 알림 전송 완료: receiverId={}, replyLetterId={}", replyLetter.getReceiverId(), replyLetter.getId());
    }

    private ReplyLetter findReplyLetter(Long replyLetterId) {
        log.debug("답장 조회 요청: replyLetterId={}", replyLetterId);

        return replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.REPLY_LETTER));
    }
}

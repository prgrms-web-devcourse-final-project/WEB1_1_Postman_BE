package postman.bottler.letter.application.service;

import static postman.bottler.notification.domain.NotificationType.KEYWORD_REPLY;

import java.util.List;
import lombok.RequiredArgsConstructor;
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
import postman.bottler.letter.exception.LetterAuthorMismatchException;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.notification.application.service.NotificationService;

@Service
@RequiredArgsConstructor
public class ReplyLetterService {

    private final ReplyLetterRepository replyLetterRepository;
    private final LetterService letterService;
    private final LetterBoxService letterBoxService;
    private final NotificationService notificationService;
    private final RedisLetterService redisLetterService;

    @Transactional
    public ReplyLetterResponseDTO createReplyLetter(
            Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId
    ) {
        validateNotExistingReply(letterId, senderId);
        ReplyLetter replyLetter = saveReplyLetter(letterId, requestDTO, senderId);
        updateLetterBoxAndSendNotification(replyLetter, requestDTO.label());
        return ReplyLetterResponseDTO.from(replyLetter);
    }

    @Transactional(readOnly = true)
    public Page<ReplyLetterSummaryResponseDTO> findReplyLetterSummaries(
            Long letterId, PageRequestDTO pageRequestDTO, Long receiverId
    ) {
        return replyLetterRepository
                .findAllByLetterIdAndReceiverId(letterId, receiverId, pageRequestDTO.toPageable())
                .map(ReplyLetterSummaryResponseDTO::from);
    }

    @Transactional(readOnly = true)
    public ReplyLetterDetailResponseDTO findReplyLetterDetail(Long replyLetterId, Long userId) {
        boolean isReplied = replyLetterRepository.existsByIdAndSenderId(replyLetterId, userId);
        ReplyLetter replyLetter = findReplyLetter(replyLetterId);
        return ReplyLetterDetailResponseDTO.from(replyLetter, isReplied);
    }

    @Transactional
    public void softDeleteReplyLetters(List<Long> replyLetterIds, Long userId) {
        List<ReplyLetter> replyLetters = replyLetterRepository.findAllByIds(replyLetterIds);

        if (replyLetters.stream().anyMatch(replyLetter -> !replyLetter.getSenderId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }

        replyLetters.forEach(
                replyLetter -> redisLetterService.deleteRecentReply(
                        replyLetter.getReceiverId(), replyLetter.getId(), replyLetter.getLabel()
                )
        );

        replyLetterRepository.softDeleteByIds(replyLetterIds);
    }

    @Transactional
    public Long softBlockLetter(Long replyLetterId) {
        ReplyLetter replyLetter = findReplyLetter(replyLetterId);
        replyLetterRepository.softBlockById(replyLetterId);
        return replyLetter.getSenderId();
    }

    @Transactional
    public boolean checkIsReplied(Long letterId, Long senderId) {
        return replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId);
    }

    private void validateNotExistingReply(Long letterId, Long senderId) {
        if (replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId)) {
            throw new DuplicateReplyLetterException();
        }
    }

    private ReplyLetter saveReplyLetter(Long letterId, ReplyLetterRequestDTO requestDTO, Long senderId) {
        ReceiverDTO receiverInfo = letterService.findReceiverInfo(letterId);
        String title = formatReplyTitle(receiverInfo.title());
        return replyLetterRepository.save(requestDTO.toDomain(title, letterId, receiverInfo.receiverId(), senderId));
    }

    private String formatReplyTitle(String title) {
        return "RE: [" + title + "]";
    }

    private void updateLetterBoxAndSendNotification(ReplyLetter replyLetter, String labelUrl) {
        saveReplyLetterToBox(replyLetter);
        redisLetterService.saveReplyToRedis(replyLetter.getLetterId(), labelUrl, replyLetter.getReceiverId());
        sendReplyNotification(replyLetter);
    }

    private void saveReplyLetterToBox(ReplyLetter replyLetter) {
        saveLetterToBox(replyLetter.getSenderId(), replyLetter, BoxType.SEND);
        saveLetterToBox(replyLetter.getReceiverId(), replyLetter, BoxType.RECEIVE);
    }

    private void saveLetterToBox(Long userId, ReplyLetter replyLetter, BoxType boxType) {
        letterBoxService.saveLetter(
                LetterBoxDTO.of(
                        userId, replyLetter.getId(), LetterType.REPLY_LETTER, boxType, replyLetter.getCreatedAt()
                )
        );
    }

    private void sendReplyNotification(ReplyLetter replyLetter) {
        notificationService.sendNotification(
                KEYWORD_REPLY,
                replyLetter.getReceiverId(),
                replyLetter.getId(),
                replyLetter.getLabel()
        );
    }

    private ReplyLetter findReplyLetter(Long replyLetterId) {
        return replyLetterRepository.findById(replyLetterId)
                .orElseThrow(() -> new LetterNotFoundException(LetterType.REPLY_LETTER));
    }
}

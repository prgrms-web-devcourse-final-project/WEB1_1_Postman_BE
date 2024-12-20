package postman.bottler.complaint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.ComplaintType;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.dto.response.ComplaintResponseDTO;
import postman.bottler.letter.service.LetterService;
import postman.bottler.letter.service.ReplyLetterService;
import postman.bottler.mapletter.service.BlockMapLetterType;
import postman.bottler.mapletter.service.MapLetterService;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.notification.service.NotificationService;
import postman.bottler.user.service.UserService;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final KeywordComplaintRepository keywordComplaintRepository;
    private final MapComplaintRepository mapComplaintRepository;
    private final KeywordReplyComplaintRepository keywordReplyComplaintRepository;
    private final MapReplyComplaintRepository mapReplyComplaintRepository;

    private final NotificationService notificationService;
    private final LetterService letterService;
    private final ReplyLetterService replyLetterService;
    private final MapLetterService mapLetterService;
    private final UserService userService;

    @Transactional
    public ComplaintResponseDTO complain(ComplaintType type, Long letterId, Long reporterId, String description) {
        ComplaintRepository repository = getRepositoryByType(type);
        Complaints complaints = repository.findByLetterId(letterId);
        Complaint complaint = Complaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            Long writer = blockLetter(type, letterId);
            notificationService.sendNotification(NotificationType.WARNING, writer, letterId, null);
            userService.updateWarningCount(writer);
        }
        return ComplaintResponseDTO.from(repository.save(complaint));
    }

    private ComplaintRepository getRepositoryByType(ComplaintType type) {
        return switch (type) {
            case MAP_LETTER -> mapComplaintRepository;
            case MAP_REPLY_LETTER -> mapReplyComplaintRepository;
            case KEYWORD_LETTER -> keywordComplaintRepository;
            case KEYWORD_REPLY_LETTER -> keywordReplyComplaintRepository;
        };
    }

    private Long blockLetter(ComplaintType type, Long letterId) {
        return switch (type) {
            case MAP_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            case MAP_REPLY_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.REPLY, letterId);
            case KEYWORD_LETTER -> letterService.blockLetter(letterId);
            case KEYWORD_REPLY_LETTER -> replyLetterService.blockLetter(letterId);
        };
    }
}

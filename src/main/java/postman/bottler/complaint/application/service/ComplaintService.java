package postman.bottler.complaint.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.application.dto.response.ComplaintResponseDTO;
import postman.bottler.complaint.application.repository.ComplaintRepository;
import postman.bottler.complaint.application.repository.KeywordComplaintRepository;
import postman.bottler.complaint.application.repository.KeywordReplyComplaintRepository;
import postman.bottler.complaint.application.repository.MapComplaintRepository;
import postman.bottler.complaint.application.repository.MapReplyComplaintRepository;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.domain.ComplaintType;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.letter.application.service.ReplyLetterService;
import postman.bottler.mapletter.application.BlockMapLetterType;
import postman.bottler.mapletter.application.service.MapLetterService;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.user.application.service.UserService;

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
        Complaint newComplaint = Complaint.create(letterId, reporterId, description);
        Complaints existingComplaints = findExistingComplaints(type, letterId);
        existingComplaints.add(newComplaint);
        if (existingComplaints.needWarning()) {
            sendWarningToWriter(type, letterId);
        }
        return ComplaintResponseDTO.from(saveComplaint(newComplaint, type));
    }

    private Complaints findExistingComplaints(ComplaintType type, Long letterId) {
        ComplaintRepository repository = getRepositoryByType(type);
        return repository.findByLetterId(letterId);
    }

    private void sendWarningToWriter(ComplaintType type, Long letterId) {
        Long writerId = blockLetter(type, letterId);
        notificationService.sendNotification(NotificationType.WARNING, writerId, letterId, null);
        userService.updateWarningCount(writerId);
    }

    private Long blockLetter(ComplaintType type, Long letterId) {
        return switch (type) {
            case MAP_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            case MAP_REPLY_LETTER -> mapLetterService.letterBlock(BlockMapLetterType.REPLY, letterId);
            case KEYWORD_LETTER -> letterService.softBlockLetter(letterId);
            case KEYWORD_REPLY_LETTER -> replyLetterService.softBlockLetter(letterId);
        };
    }

    private Complaint saveComplaint(Complaint complaint, ComplaintType type) {
        ComplaintRepository repository = getRepositoryByType(type);
        return repository.save(complaint);
    }

    private ComplaintRepository getRepositoryByType(ComplaintType type) {
        return switch (type) {
            case MAP_LETTER -> mapComplaintRepository;
            case MAP_REPLY_LETTER -> mapReplyComplaintRepository;
            case KEYWORD_LETTER -> keywordComplaintRepository;
            case KEYWORD_REPLY_LETTER -> keywordReplyComplaintRepository;
        };
    }
}

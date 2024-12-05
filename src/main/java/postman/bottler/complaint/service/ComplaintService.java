package postman.bottler.complaint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordComplaint;
import postman.bottler.complaint.domain.KeywordReplyComplaint;
import postman.bottler.complaint.domain.MapComplaint;
import postman.bottler.complaint.domain.MapReplyComplaint;
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
    public ComplaintResponseDTO complainKeywordLetter(Long letterId, Long reporterId,
                                                      String description) {
        Complaints complaints = keywordComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        KeywordComplaint complaint = KeywordComplaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            Long writer = 1L; // letterService.blockLetter(letterId);
            notificationService.sendNotification(NotificationType.WARNING, writer, letterId);
            userService.updateWarningCount(writer);
        }
        return ComplaintResponseDTO.from(keywordComplaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO complainMapLetter(Long letterId, Long reporterId,
                                                  String description) {
        Complaints complaints = mapComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        MapComplaint complaint = MapComplaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            Long writer = mapLetterService.letterBlock(BlockMapLetterType.MAP_LETTER, letterId);
            notificationService.sendNotification(NotificationType.WARNING, writer, letterId);
            userService.updateWarningCount(writer);
        }
        return ComplaintResponseDTO.from(mapComplaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO complainKeywordReplyLetter(Long letterId, Long reporterId,
                                                           String description) {
        Complaints complaints = keywordReplyComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        KeywordReplyComplaint complaint = KeywordReplyComplaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            Long writer = 1L; // replyLetterService.blockLetter(letterId);
            notificationService.sendNotification(NotificationType.WARNING, writer, letterId);
            userService.updateWarningCount(writer);
        }
        return ComplaintResponseDTO.from(keywordReplyComplaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO complainMapReplyLetter(Long letterId, Long reporterId,
                                                       String description) {
        Complaints complaints = mapReplyComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        MapReplyComplaint complaint = MapReplyComplaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            Long writer = mapLetterService.letterBlock(BlockMapLetterType.REPLY, letterId);
            notificationService.sendNotification(NotificationType.WARNING, writer, letterId);
            userService.updateWarningCount(writer);
        }
        return ComplaintResponseDTO.from(mapReplyComplaintRepository.save(complaint));
    }
}

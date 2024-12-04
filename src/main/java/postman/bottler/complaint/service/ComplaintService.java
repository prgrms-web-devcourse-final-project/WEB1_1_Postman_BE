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
import postman.bottler.notification.service.NotificationService;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final KeywordComplaintRepository keywordComplaintRepository;
    private final MapComplaintRepository mapComplaintRepository;
    private final KeywordReplyComplaintRepository keywordReplyComplaintRepository;
    private final MapReplyComplaintRepository mapReplyComplaintRepository;

    private final NotificationService notificationService;

    @Transactional
    public ComplaintResponseDTO complainKeywordLetter(Long letterId, Long reporterId,
                                                      String description) {
        Complaints complaints = keywordComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        KeywordComplaint complaint = KeywordComplaint.create(letterId, reporterId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            // TODO 편지 블락 처리
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
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
            // TODO 편지 블락 처리
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
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
            // TODO 편지 블락 처리
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
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
            // TODO 편지 블락 처리
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
        }
        return ComplaintResponseDTO.from(mapReplyComplaintRepository.save(complaint));
    }
}

package postman.bottler.complaint.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordComplaint;
import postman.bottler.complaint.domain.MapComplaint;
import postman.bottler.complaint.domain.ReplyComplaint;
import postman.bottler.complaint.dto.response.ComplaintResponseDTO;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final KeywordComplaintRepository keywordComplaintRepository;
    private final MapComplaintRepository mapComplaintRepository;
    private final ReplyComplaintRepository replyComplaintRepository;

    @Transactional
    public ComplaintResponseDTO complainKeywordLetter(Long letterId, Long reporterId, Long reportedUserId,
                                                      String description) {
        Complaints complaints = keywordComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        KeywordComplaint complaint = KeywordComplaint.create(letterId, reporterId, reportedUserId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
            // TODO 편지 블락 처리
        }
        return ComplaintResponseDTO.from(keywordComplaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO complainMapLetter(Long letterId, Long reporterId, Long reportedUserId,
                                                  String description) {
        Complaints complaints = mapComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        MapComplaint complaint = MapComplaint.create(letterId, reporterId, reportedUserId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
            // TODO 편지 블락 처리
        }
        return ComplaintResponseDTO.from(mapComplaintRepository.save(complaint));
    }

    @Transactional
    public ComplaintResponseDTO complainReplyLetter(Long letterId, Long reporterId, Long reportedUserId,
                                                    String description) {
        Complaints complaints = replyComplaintRepository.findByLetterId(letterId);
        complaints.validateDuplication(reporterId);
        ReplyComplaint complaint = ReplyComplaint.create(letterId, reporterId, reportedUserId, description);
        complaints.add(complaint);
        if (complaints.needsWarningNotification()) {
            // TODO 편지 작성자에게 경고 알림
            // TODO 편지 작성자 경고 횟수 증가
            // TODO 편지 블락 처리
        }
        return ComplaintResponseDTO.from(replyComplaintRepository.save(complaint));
    }
}

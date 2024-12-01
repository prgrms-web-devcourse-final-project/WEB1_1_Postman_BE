package postman.bottler.complaint.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.KeywordComplaint;
import postman.bottler.complaint.domain.KeywordReplyComplaint;
import postman.bottler.complaint.domain.MapComplaint;
import postman.bottler.complaint.domain.MapReplyComplaint;
import postman.bottler.complaint.dto.response.ComplaintResponseDTO;
import postman.bottler.complaint.exception.DuplicateComplainException;

@ExtendWith(MockitoExtension.class)
@DisplayName("신고 서비스 테스트")
public class ComplaintServiceTest {
    @InjectMocks
    private ComplaintService complaintService;
    @Mock
    private KeywordComplaintRepository keywordComplaintRepository;
    @Mock
    private MapComplaintRepository mapComplaintRepository;
    @Mock
    private KeywordReplyComplaintRepository keywordReplyComplaintRepository;
    @Mock
    private MapReplyComplaintRepository mapReplyComplaintRepository;

    @Test
    @DisplayName("키워드 편지를 신고한다.")
    public void complainKeywordLetter() {
        // GIVEN
        when(keywordComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()));
        when(keywordComplaintRepository.save(any())).thenReturn(KeywordComplaint.of(1L, 1L, 1L, "욕설사용"));

        // WHEN
        ComplaintResponseDTO response = complaintService.complainKeywordLetter(1L, 1L, "욕설 사용");

        // THEN
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("같은 키워드 편지를 2회 이상 신고 시도할 경우, DuplicateComplainException을 발생시킨다.")
    public void duplicateKeywordComplain() {
        // GIVEN
        KeywordComplaint complaint = KeywordComplaint.of(1L, 1L, 1L, "욕설사용");
        when(keywordComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()))
                .thenReturn(Complaints.from(List.of(complaint)));
        when(keywordComplaintRepository.save(any())).thenReturn(complaint);
        complaintService.complainKeywordLetter(1L, 1L, "욕설 사용");

        // WHEN - THEN
        assertThatThrownBy(() -> complaintService.complainKeywordLetter(1L, 1L, "욕설 사용"))
                .isInstanceOf(DuplicateComplainException.class);
    }

    @Test
    @DisplayName("지도 편지를 신고한다.")
    public void complainMapLetter() {
        // GIVEN
        when(mapComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()));
        when(mapComplaintRepository.save(any())).thenReturn(MapComplaint.of(1L, 1L, 1L, "욕설사용"));

        // WHEN
        ComplaintResponseDTO response = complaintService.complainMapLetter(1L, 1L, "욕설 사용");

        // THEN
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("같은 지도 편지를 2회 이상 신고 시도할 경우, DuplicateComplainException을 발생시킨다.")
    public void duplicateMapComplain() {
        // GIVEN
        MapComplaint complaint = MapComplaint.of(1L, 1L, 1L, "욕설사용");
        when(mapComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()))
                .thenReturn(Complaints.from(List.of(complaint)));
        when(mapComplaintRepository.save(any())).thenReturn(complaint);
        complaintService.complainMapLetter(1L, 1L, "욕설 사용");

        // WHEN - THEN
        assertThatThrownBy(() -> complaintService.complainMapLetter(1L, 1L, "욕설 사용"))
                .isInstanceOf(DuplicateComplainException.class);
    }

    @Test
    @DisplayName("키워드 답장 편지를 신고한다.")
    public void complainKeywordReplyLetter() {
        // GIVEN
        when(keywordReplyComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()));
        when(keywordReplyComplaintRepository.save(any())).thenReturn(KeywordReplyComplaint.of(1L, 1L, 1L, "욕설사용"));

        // WHEN
        ComplaintResponseDTO response = complaintService.complainKeywordReplyLetter(1L, 1L, "욕설 사용");

        // THEN
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("같은 답장 편지를 2회 이상 신고 시도할 경우, DuplicateComplainException을 발생시킨다.")
    public void duplicateKeywordReplyComplain() {
        // GIVEN
        KeywordReplyComplaint complaint = KeywordReplyComplaint.of(1L, 1L, 1L, "욕설사용");
        when(keywordReplyComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()))
                .thenReturn(Complaints.from(List.of(complaint)));
        when(keywordReplyComplaintRepository.save(any())).thenReturn(complaint);
        complaintService.complainKeywordReplyLetter(1L, 1L, "욕설 사용");

        // WHEN - THEN
        assertThatThrownBy(() -> complaintService.complainKeywordReplyLetter(1L, 1L, "욕설 사용"))
                .isInstanceOf(DuplicateComplainException.class);
    }

    @Test
    @DisplayName("지도 답장 편지를 신고한다.")
    public void complainMapReplyLetter() {
        // GIVEN
        when(mapReplyComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()));
        when(mapReplyComplaintRepository.save(any())).thenReturn(MapReplyComplaint.of(1L, 1L, 1L, "욕설사용"));

        // WHEN
        ComplaintResponseDTO response = complaintService.complainMapReplyLetter(1L, 1L, "욕설 사용");

        // THEN
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("같은 답장 편지를 2회 이상 신고 시도할 경우, DuplicateComplainException을 발생시킨다.")
    public void duplicateMapReplyComplain() {
        // GIVEN
        MapReplyComplaint complaint = MapReplyComplaint.of(1L, 1L, 1L, "욕설사용");
        when(mapReplyComplaintRepository.findByLetterId(1L)).thenReturn(Complaints.from(new ArrayList<>()))
                .thenReturn(Complaints.from(List.of(complaint)));
        when(mapReplyComplaintRepository.save(any())).thenReturn(complaint);
        complaintService.complainMapReplyLetter(1L, 1L, "욕설 사용");

        // WHEN - THEN
        assertThatThrownBy(() -> complaintService.complainMapReplyLetter(1L, 1L, "욕설 사용"))
                .isInstanceOf(DuplicateComplainException.class);
    }
}

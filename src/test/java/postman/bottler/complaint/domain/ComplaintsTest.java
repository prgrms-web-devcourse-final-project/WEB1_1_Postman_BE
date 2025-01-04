package postman.bottler.complaint.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.complaint.exception.DuplicateComplainException;

@DisplayName("신고 테스트")
public class ComplaintsTest {
    @DisplayName("기존 신고에 새로운 신고를 추가한다.")
    @Test
    void add() {
        // given
        List<Complaint> complaintList = new ArrayList<>();
        complaintList.add(Complaint.create(1L, 1L, "욕설 사용"));
        Complaints complaints = Complaints.from(complaintList);

        // when
        complaints.add(Complaint.create(1L, 2L, "욕설 사용"));

        // then
        assertThat(complaints.getComplaints()).hasSize(2);
    }

    @Test
    @DisplayName("한 편지의 신고자가 2명일 경우, DuplicateComplainException 예외를 발생시킨다.")
    public void addWithDuplicateComplaint() {
        // GIVEN
        List<Complaint> complaintList = new ArrayList<>();
        complaintList.add(Complaint.create(1L, 1L, "욕설 사용"));
        Complaints complaints = Complaints.from(complaintList);

        Complaint duplicate = Complaint.create(1L, 1L, "욕설 사용");

        // WHEN -  THEN
        assertThatThrownBy(() -> complaints.add(duplicate))
                .isInstanceOf(DuplicateComplainException.class);
    }

    @DisplayName("신고 개수가 경고 조건 미만일 경우, 경고가 불필요함을 알린다.")
    @Test
    void needWarning() {
        // given
        Complaint complaint = Complaint.create(1L, 1L, "욕설 사용");
        Complaints complaints = Complaints.from(List.of(complaint));

        // when
        Boolean result = complaints.needWarning();

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("신고의 개수가 경고 조건에 해당하는 경우, 경고가 필요함을 알린다.")
    @Test
    void needWarningWithWarningCondition() {
        // given
        Complaint complaint1 = Complaint.create(1L, 1L, "욕설 사용");
        Complaint complaint2 = Complaint.create(1L, 2L, "욕설 사용");
        Complaint complaint3 = Complaint.create(1L, 3L, "욕설 사용");
        Complaints complaints = Complaints.from(List.of(complaint1, complaint2, complaint3));

        // when
        Boolean result = complaints.needWarning();

        // then
        assertThat(result).isTrue();
    }
}

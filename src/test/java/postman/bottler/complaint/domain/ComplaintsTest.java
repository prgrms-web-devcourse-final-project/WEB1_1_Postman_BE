package postman.bottler.complaint.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.complaint.exception.DuplicateComplainException;

@DisplayName("신고 테스트")
public class ComplaintsTest {
    @Test
    @DisplayName("한 편지의 신고자가 2명일 경우, DuplicateComplainException 예외를 발생시킨다.")
    public void duplicateException() {
        // GIVEN
        Complaints complaints = Complaints.from(List.of(KeywordComplaint.create(1L, 1L, 2L, "욕설 사용")));

        // WHEN -  THEN
        assertThatThrownBy(() -> complaints.validateDuplication(1L)).isInstanceOf(DuplicateComplainException.class);
    }
}

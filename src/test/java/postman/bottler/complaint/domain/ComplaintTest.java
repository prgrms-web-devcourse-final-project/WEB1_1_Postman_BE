package postman.bottler.complaint.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import postman.bottler.complaint.exception.DuplicateComplainException;

class ComplaintTest {

    @DisplayName("새로운 신고를 생성한다.")
    @Test
    void create() {
        // when
        Complaint complaint = Complaint.create(1L, 1L, "부적절한 편지 내용");

        // then
        assertThat(complaint)
                .extracting("id", "letterId", "reporterId", "description")
                .containsExactlyInAnyOrder(null, 1L, 1L, "부적절한 편지 내용");
    }

    @DisplayName("기존에 저장된 신고를 통해 객체를 생성한다.")
    @Test
    void of() {
        // given
        LocalDateTime now = LocalDateTime.now();

        // when
        Complaint complaint = Complaint.of(1L, 1L, 1L, "부적절한 편지 내용", now);

        // then
        assertThat(complaint)
                .extracting("id", "letterId", "reporterId", "description", "createdAt")
                .containsExactlyInAnyOrder(1L, 1L, 1L, "부적절한 편지 내용", now);
    }

    @DisplayName("사용자가 이미 해당 편지를 신고한 경우, 예외가 발생한다.")
    @Test
    void validateDuplicateComplaint() {
        // given
        Complaint complaint = Complaint.create(1L, 1L, "부적절한 편지 내용");

        // when then
        assertThatThrownBy(() -> complaint.validateDuplicateComplaint(1L, 1L))
                .isInstanceOf(DuplicateComplainException.class);
    }
}

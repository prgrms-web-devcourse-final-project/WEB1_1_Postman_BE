package postman.bottler.complaint.infra;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.domain.Complaints;
import postman.bottler.complaint.domain.ReplyComplaint;
import postman.bottler.complaint.service.ReplyComplaintRepository;

@DisplayName("답장 편지 리포지토리 테스트")
@SpringBootTest
@Transactional
public class ReplyComplaintRepositoryTest {
    @Autowired
    private ReplyComplaintRepository replyComplaintRepository;

    @Test
    @DisplayName("편지 ID로 찾은 신고 객체들은 mutable이어야 한다.")
    public void mutable() {
        // GIVEN
        replyComplaintRepository.save(ReplyComplaint.create(1L, 1L, 2L, "설명"));

        // WHEN
        Complaints find = replyComplaintRepository.findByLetterId(1L);

        // THEN
        Assertions.assertDoesNotThrow(() -> find.add(ReplyComplaint.create(1L, 1L, 2L, "설명")));
    }
}

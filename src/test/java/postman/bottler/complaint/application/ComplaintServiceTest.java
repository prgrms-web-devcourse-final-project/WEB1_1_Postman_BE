package postman.bottler.complaint.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static org.mockito.BDDMockito.given;
import static postman.bottler.complaint.domain.ComplaintType.KEYWORD_LETTER;
import static postman.bottler.complaint.domain.ComplaintType.KEYWORD_REPLY_LETTER;
import static postman.bottler.complaint.domain.ComplaintType.MAP_LETTER;
import static postman.bottler.complaint.domain.ComplaintType.MAP_REPLY_LETTER;
import static postman.bottler.notification.domain.NotificationType.WARNING;

import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.complaint.application.dto.response.ComplaintResponseDTO;
import postman.bottler.complaint.application.repository.KeywordComplaintRepository;
import postman.bottler.complaint.application.service.ComplaintService;
import postman.bottler.complaint.domain.Complaint;
import postman.bottler.complaint.exception.DuplicateComplainException;
import postman.bottler.letter.application.service.LetterService;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.user.application.service.UserService;

@SpringBootTest
@DisplayName("신고 서비스 테스트")
@Transactional
public class ComplaintServiceTest {
    @Autowired
    private ComplaintService complaintService;
    @Autowired
    private KeywordComplaintRepository keywordComplaintRepository;

    @MockBean
    private NotificationService notificationService;
    @MockBean
    private LetterService letterService;
    @MockBean
    private UserService userService;

    @DisplayName("키워드 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainKeywordLetter() {
        return List.of(
                dynamicTest("키워드 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponseDTO response = complaintService.complain(KEYWORD_LETTER, 1L, 1L, "욕설 사용");

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintService.complain(KEYWORD_LETTER, 1L, 1L, "욕설 사용"))
                            .isInstanceOf(DuplicateComplainException.class);
                })
        );
    }

    @DisplayName("키워드 답장 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainKeywordReplyLetter() {
        return List.of(
                dynamicTest("키워드 답장 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponseDTO response = complaintService.complain(KEYWORD_REPLY_LETTER, 1L, 1L, "욕설 사용");

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 답장 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintService.complain(KEYWORD_REPLY_LETTER, 1L, 1L, "욕설 사용"))
                            .isInstanceOf(DuplicateComplainException.class);
                })
        );
    }

    @DisplayName("지도 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainMapLetter() {
        return List.of(
                dynamicTest("지도 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponseDTO response = complaintService.complain(MAP_LETTER, 1L, 1L, "욕설 사용");

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 답장 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintService.complain(MAP_LETTER, 1L, 1L, "욕설 사용"))
                            .isInstanceOf(DuplicateComplainException.class);
                })
        );
    }

    @DisplayName("지도 편지 시나리오")
    @TestFactory
    Collection<DynamicTest> complainMapReplyLetter() {
        return List.of(
                dynamicTest("지도 편지를 신고한다.", () -> {
                    // when
                    ComplaintResponseDTO response = complaintService.complain(MAP_REPLY_LETTER, 1L, 1L, "욕설 사용");

                    // then
                    assertThat(response.id()).isNotNull();
                    assertThat(response.description()).isEqualTo("욕설 사용");
                }),
                dynamicTest("한 유저가 같은 키워드 답장 편지를 2회 이상 신고 시도할 경우, 예외가 발생한다.", () -> {
                    // when then
                    assertThatThrownBy(() -> complaintService.complain(MAP_REPLY_LETTER, 1L, 1L, "욕설 사용"))
                            .isInstanceOf(DuplicateComplainException.class);
                })
        );
    }

    @DisplayName("경고 알림이 필요한 경우, 작성자의 경고 횟수를 증가시킨다.")
    @Test
    void needWarningWithUserService() {
        // given
        keywordComplaintRepository.save(Complaint.create(1L, 1L, "욕설 사용"));
        keywordComplaintRepository.save(Complaint.create(1L, 2L, "욕설 사용"));
        Long writerId = 5L;

        given(letterService.blockLetter(1L))
                .willReturn(writerId);

        // when
        complaintService.complain(KEYWORD_LETTER, 1L, 3L, "욕설 사용");

        // then
        Mockito.verify(userService, Mockito.times(1))
                .updateWarningCount(writerId);
    }

    @DisplayName("경고 알림이 필요한 경우, 작성자에게 알림을 전송한다.")
    @Test
    void needWarningWithNotificationService() {
        // given
        keywordComplaintRepository.save(Complaint.create(1L, 1L, "욕설 사용"));
        keywordComplaintRepository.save(Complaint.create(1L, 2L, "욕설 사용"));
        Long writerId = 5L;

        given(letterService.blockLetter(1L))
                .willReturn(writerId);

        // when
        complaintService.complain(KEYWORD_LETTER, 1L, 3L, "욕설 사용");

        // then
        Mockito.verify(notificationService, Mockito.times(1))
                .sendNotification(WARNING, writerId, 1L, null);
    }
}

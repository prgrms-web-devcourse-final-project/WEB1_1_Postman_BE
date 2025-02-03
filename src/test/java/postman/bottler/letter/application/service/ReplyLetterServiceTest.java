package postman.bottler.letter.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.TestBase;
import postman.bottler.letter.application.dto.ReceiverDTO;
import postman.bottler.letter.application.dto.request.ReplyLetterRequestDTO;
import postman.bottler.letter.application.dto.response.ReplyLetterResponseDTO;
import postman.bottler.letter.application.repository.ReplyLetterRepository;
import postman.bottler.letter.domain.ReplyLetter;
import postman.bottler.letter.exception.DuplicateReplyLetterException;
import postman.bottler.notification.application.service.NotificationService;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ReplyLetterServiceTest extends TestBase {

    @InjectMocks
    private ReplyLetterService replyLetterService;

    @Mock
    private ReplyLetterRepository replyLetterRepository;

    @Mock
    private LetterService letterService;

    @Mock
    private NotificationService notificationService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Mock
    private LetterBoxService letterBoxService;

    @Nested
    @DisplayName("답장 생성")
    class CreateReplyLetter {

        @Test
        @DisplayName("성공적으로 답장을 생성한다")
        void createReplyLetterSuccess() {
            // given
            Long letterId = 10L;
            Long senderId = 100L;
            ReplyLetterRequestDTO requestDTO = new ReplyLetterRequestDTO("내용", "폰트", "편지지", "label");

            when(replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId)).thenReturn(false);

            ReceiverDTO receiverDTO = new ReceiverDTO(200L, "테스트 제목");
            when(letterService.findReceiverInfo(letterId)).thenReturn(receiverDTO);

            ReplyLetter replyLetter = requestDTO.toDomain("RE: [테스트 제목]", letterId, receiverDTO.receiverId(), senderId);
            when(replyLetterRepository.save(any())).thenReturn(replyLetter);

            when(redisTemplate.opsForList()).thenReturn(listOperations);
            when(listOperations.size(anyString())).thenReturn(0L);
            when(listOperations.range(anyString(), eq(0L), eq(-1L))).thenReturn(List.of());

            // when
            ReplyLetterResponseDTO result = replyLetterService.createReplyLetter(letterId, requestDTO, senderId);

            // then
            assertThat(result).isNotNull();
            verify(replyLetterRepository, times(1)).save(any());
            verify(listOperations, times(1)).leftPush(anyString(), anyString());
            verify(notificationService, times(1)).sendNotification(any(), eq(200L), eq(10L), any());
        }

        @Test
        @DisplayName("중복 답장 생성 시 예외를 던진다")
        void createReplyLetterDuplicate() {
            // given
            Long letterId = 10L;
            Long senderId = 100L;
            ReplyLetterRequestDTO requestDTO = new ReplyLetterRequestDTO("내용", "폰트", "편지지", "label");

            when(replyLetterRepository.existsByLetterIdAndSenderId(letterId, senderId)).thenReturn(true);

            // when & then
            assertThrows(DuplicateReplyLetterException.class,
                    () -> replyLetterService.createReplyLetter(letterId, requestDTO, senderId));

            verify(replyLetterRepository, times(1)).existsByLetterIdAndSenderId(letterId, senderId);
        }
    }

    @Nested
    @DisplayName("Redis 저장")
    class SaveReplyToRedis {

        @Test
        @DisplayName("Redis에 답장을 저장한다")
        void saveReplyToRedis() {
            // given
            Long letterId = 10L;
            Long senderId = 100L;
            ReplyLetterRequestDTO requestDTO = new ReplyLetterRequestDTO("내용", "폰트", "편지지", "label");

            String key = "REPLY:200";
            String value = "KEYWORD:10:label";

            ReceiverDTO receiverDTO = new ReceiverDTO(200L, "테스트 제목");
            when(letterService.findReceiverInfo(letterId)).thenReturn(receiverDTO);

            ReplyLetter replyLetter = requestDTO.toDomain("RE: [테스트 제목]", letterId, receiverDTO.receiverId(), senderId);
            when(replyLetterRepository.save(any())).thenReturn(replyLetter);

            when(redisTemplate.opsForList()).thenReturn(listOperations);
            when(listOperations.size(key)).thenReturn(5L);
            when(listOperations.range(key, 0, -1)).thenReturn(List.of("EXISTING_VALUE"));

            // when
            replyLetterService.createReplyLetter(letterId, requestDTO, senderId);

            // then
            verify(listOperations, times(1)).leftPush(key, value);
        }
    }

    @Nested
    @DisplayName("답장 상세 조회")
    class FindReplyLetterDetail {

        @Test
        @DisplayName("성공적으로 답장 상세 정보를 조회한다")
        void findReplyLetterDetailSuccess() {
            // given
            Long replyLetterId = 1L;
            Long userId = 100L;

            ReplyLetter mockReplyLetter = ReplyLetter.builder()
                    .id(replyLetterId)
                    .title("RE: 제목")
                    .content("내용")
                    .build();

            when(replyLetterRepository.existsByIdAndSenderId(replyLetterId, userId)).thenReturn(true);
            when(replyLetterRepository.findById(replyLetterId)).thenReturn(Optional.of(mockReplyLetter));

            // when
            var result = replyLetterService.findReplyLetterDetail(replyLetterId, userId);

            // then
            assertThat(result).isNotNull();
            assertThat(result.replyLetterId()).isEqualTo(replyLetterId);
            verify(replyLetterRepository, times(1)).findById(replyLetterId);
        }
    }
}

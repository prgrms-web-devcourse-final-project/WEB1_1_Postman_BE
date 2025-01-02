package postman.bottler.mapletter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static postman.bottler.notification.domain.NotificationType.MAP_REPLY;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.domain.ReplyMapLetter;
import postman.bottler.mapletter.dto.FindReceivedMapLetterDTO;
import postman.bottler.mapletter.dto.FindSentMapLetter;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.FindNearbyLettersResponseDTO;
import postman.bottler.mapletter.dto.response.FindReceivedMapLetterResponseDTO;
import postman.bottler.mapletter.dto.response.OneLetterResponseDTO;
import postman.bottler.mapletter.exception.LetterAlreadyReplyException;
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.user.service.UserService;

@ExtendWith(MockitoExtension.class)
class MapLetterServiceTest {
    @InjectMocks
    private MapLetterService mapLetterService;
    @Mock
    private MapLetterRepository mapLetterRepository;
    @Mock
    private UserService userService;
    @Mock
    private NotificationService notificationService;
    @Mock
    private ReplyMapLetterRepository replyMapLetterRepository;
    @Mock
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void setup() {
        Mockito.reset(mapLetterRepository); // Mock 상태 초기화
    }

    @Test
    @DisplayName("퍼블릭 편지 생성에 성공한다.")
    void createPublicMapLetterTest() {
        //given
        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO("Test Title", "TestContent",
                "장소 설명", new BigDecimal("37.5665"), new BigDecimal("127.23456"), "맑은고딕", "www.paper.com",
                "www.label.com");
        Long userId = 1L;
        MapLetter expectedMapLetter = MapLetter.createPublicMapLetter(requestDTO, userId);

        when(mapLetterRepository.save(any(MapLetter.class))).thenReturn(expectedMapLetter);

        //when
        MapLetter actualMapLetter = mapLetterService.createPublicMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(), actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(), actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(), actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(), actualMapLetter.getLongitude());
        assertEquals(expectedMapLetter.getDescription(), actualMapLetter.getDescription());

        verify(mapLetterRepository, Mockito.times(1)).save(any(MapLetter.class));
    }

    @Test
    @DisplayName("타겟 편지 생성에 성공한다.")
    void createTargetLetterTest() {
        String targetUser = "타겟";
        Long targetUserId = 2L;

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO("Test Title", "TestContent",
                "장소 설명", new BigDecimal("37.5665"), new BigDecimal("127.23456"), "맑은고딕", "www.paper.com",
                "www.label.com", targetUser //임시 타겟
        );

        Long userId = 1L;
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId, targetUserId);

        when(mapLetterRepository.save(any(MapLetter.class))).thenReturn(expectedMapLetter);
        when(userService.getUserIdByNickname(targetUser)).thenReturn(targetUserId);

        //when
        MapLetter actualMapLetter = mapLetterService.createTargetMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(), actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(), actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(), actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(), actualMapLetter.getLongitude());
        assertEquals(expectedMapLetter.getTargetUserId(), actualMapLetter.getTargetUserId());
        assertEquals(expectedMapLetter.getDescription(), actualMapLetter.getDescription());

        verify(mapLetterRepository, Mockito.times(1)).save(any(MapLetter.class));
    }

    @Test
    @DisplayName("편지가 PUBLIC일 경우 편지 상세 조회에 성공한다.")
    void findPublicMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("127.23456");

        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO("퍼블릭 편지 상세 조회 테스트", "편지 내용",
                "장소 설명", latitude, longitude, "맑은고딕", "www.paper.com", "www.label.com");
        MapLetter publicLetter = MapLetter.createPublicMapLetter(requestDTO, userId);

        when(mapLetterRepository.findById(letterId)).thenReturn(publicLetter);

        // when
        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(letterId, userId, latitude, longitude);

        // then
        assertNotNull(response);
        assertEquals("퍼블릭 편지 상세 조회 테스트", response.title());
        assertEquals("편지 내용", response.content());
        assertEquals("맑은고딕", response.font());
        assertEquals("장소 설명", response.description());
        assertEquals("www.paper.com", response.paper());
        assertEquals("www.label.com", response.label());
        verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("편지가 TARGET이고, TARGET ID와 USER ID가 동일하면 편지 상세 조회에 성공한다.")
    void findTargetMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;
        Long targetUserId = 7L;
        String targetUser = "타겟";

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("127.23456");

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO("프라이빗 편지 편지 상세 조회 테스트",
                "편지 내용", "장소 설명", latitude, longitude, "맑은고딕", "www.paper.com", "www.label.com", targetUser);
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId, targetUserId);

        expectedMapLetter = MapLetter.builder().id(letterId) // id를 명시적으로 설정
                .title(expectedMapLetter.getTitle()).content(expectedMapLetter.getContent())
                .description(expectedMapLetter.getDescription()).latitude(expectedMapLetter.getLatitude())
                .longitude(expectedMapLetter.getLongitude()).font(expectedMapLetter.getFont())
                .paper(expectedMapLetter.getPaper()).label(expectedMapLetter.getLabel())
                .type(expectedMapLetter.getType()).targetUserId(expectedMapLetter.getTargetUserId())
                .createUserId(expectedMapLetter.getCreateUserId()).createdAt(expectedMapLetter.getCreatedAt())
                .updatedAt(expectedMapLetter.getUpdatedAt()).isDeleted(expectedMapLetter.isDeleted())
                .isBlocked(expectedMapLetter.isBlocked()).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);

        // when
        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(expectedMapLetter.getId(), targetUserId,
                latitude, longitude);

        // then
        assertNotNull(response);
        assertEquals(MapLetterType.PRIVATE, expectedMapLetter.getType()); //타입이 PRIVATE인지 조회
        assertEquals(targetUserId, expectedMapLetter.getTargetUserId()); //타겟이 맞는지 조회
        assertEquals("프라이빗 편지 편지 상세 조회 테스트", response.title());
        assertEquals("장소 설명", response.description());
        assertEquals("편지 내용", response.content());
        assertEquals("맑은고딕", response.font());
        assertEquals("www.paper.com", response.paper());
        assertEquals("www.label.com", response.label());
        verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("편지가 TARGET이고, CREATE USER ID와 USER ID가 동일하면 편지 상세 조회에 성공한다.")
    void findCreateUserTargetMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;
        Long targetUserId = 7L;
        String targetUser = "타겟";

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("127.23456");

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO("프라이빗 편지 편지 상세 조회 테스트",
                "편지 내용", "장소 설명", latitude, longitude, "맑은고딕", "www.paper.com", "www.label.com", targetUser);
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId, targetUserId);

        expectedMapLetter = MapLetter.builder().id(letterId) // id를 명시적으로 설정
                .title(expectedMapLetter.getTitle()).content(expectedMapLetter.getContent())
                .description(expectedMapLetter.getDescription()).latitude(expectedMapLetter.getLatitude())
                .longitude(expectedMapLetter.getLongitude()).font(expectedMapLetter.getFont())
                .paper(expectedMapLetter.getPaper()).label(expectedMapLetter.getLabel())
                .type(expectedMapLetter.getType()).targetUserId(expectedMapLetter.getTargetUserId())
                .createUserId(expectedMapLetter.getCreateUserId()).createdAt(expectedMapLetter.getCreatedAt())
                .updatedAt(expectedMapLetter.getUpdatedAt()).isDeleted(expectedMapLetter.isDeleted())
                .isBlocked(expectedMapLetter.isBlocked()).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);

        // when
        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(expectedMapLetter.getId(), userId, latitude,
                longitude);

        // then
        assertNotNull(response);
        assertEquals(MapLetterType.PRIVATE, expectedMapLetter.getType()); //타입이 PRIVATE인지 조회
        assertEquals(userId, expectedMapLetter.getCreateUserId());
        verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("편지가 TARGET이고 TARGET ID와 USER ID가 일치하지 않을 경우 편지 상세 조회에 실패한다.")
    void findTargetMapLetterUnauthorized() {
        // given
        Long letterId = 1L;
        Long userId = 2L;
        Long targetUserId = 7L;
        Long unAuthorizedUserId = 10L;
        String targetUser = "타겟";

        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("127.23456");

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO("프라이빗 편지 편지 상세 조회 테스트",
                "편지 내용", "장소 설명", latitude, longitude, "맑은고딕", "www.paper.com", "www.label.com", targetUser);
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId, targetUserId);

        expectedMapLetter = MapLetter.builder().id(letterId) // id를 명시적으로 설정
                .title(expectedMapLetter.getTitle()).content(expectedMapLetter.getContent())
                .description(expectedMapLetter.getDescription()).latitude(expectedMapLetter.getLatitude())
                .longitude(expectedMapLetter.getLongitude()).font(expectedMapLetter.getFont())
                .paper(expectedMapLetter.getPaper()).label(expectedMapLetter.getLabel())
                .type(expectedMapLetter.getType()).targetUserId(expectedMapLetter.getTargetUserId())
                .createUserId(expectedMapLetter.getCreateUserId()).createdAt(expectedMapLetter.getCreatedAt())
                .updatedAt(expectedMapLetter.getUpdatedAt()).isDeleted(expectedMapLetter.isDeleted())
                .isBlocked(expectedMapLetter.isBlocked()).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);

        // when & then
        Exception exception = assertThrows(CommonForbiddenException.class,
                () -> mapLetterService.findOneMapLetter(letterId, unAuthorizedUserId, latitude, longitude));

        assertEquals("편지를 볼 수 있는 권한이 없습니다.", exception.getMessage());
        verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("PUBLIC 편지 삭제에 성공하고 isDeleted 상태가 true로 변경된다.")
    void deletePublicMapLetterAndVerifyIsDeletedTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        MapLetter mockLetter = MapLetter.builder().id(letterId).title("테스트 편지").content("삭제 테스트").createUserId(userId)
                .type(MapLetterType.PUBLIC).isDeleted(false) // 초기 상태는 false
                .build();

        when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        Mockito.doAnswer(invocation -> {
            mockLetter.updateDelete(true);
            return null;
        }).when(mapLetterRepository).softDelete(letterId);

        // when
        mapLetterService.deleteMapLetter(List.of(letterId), userId);

        // then
        assertTrue(mockLetter.isDeleted(), "isDeleted가 true로 변경되어야 합니다.");
        verify(mapLetterRepository, Mockito.times(1)).softDelete(letterId);
    }

    @Test
    @DisplayName("PUBLIC 편지 삭제 권한이 없을 경우 CommonForbiddenException 예외를 발생시킨다.")
    void deletePublicMapLetterWithoutPermissionTest() {
        // given
        Long letterId = 1L;
        Long userId = 3L; // 작성자가 아닌 사용자 ID
        Long userId2 = 2L; // 작성자

        MapLetter mockLetter = MapLetter.builder().id(letterId).title("테스트 편지").content("삭제 테스트").createUserId(userId2)
                .type(MapLetterType.PUBLIC).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when & then
        CommonForbiddenException exception = assertThrows(CommonForbiddenException.class,
                () -> mapLetterService.deleteMapLetter(List.of(letterId), userId));

        assertEquals("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.", exception.getMessage());

        verify(mapLetterRepository, Mockito.times(0)).softDelete(Mockito.anyLong());
    }

    @Test
    @DisplayName("TARGET 편지 삭제에 성공하고, isDeleted 상태가 true로 변경된다.")
    void deleteTargetMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        MapLetter mockLetter = MapLetter.builder().id(letterId).title("테스트 편지").content("삭제 테스트").createUserId(userId)
                .type(MapLetterType.PRIVATE).isDeleted(false).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        Mockito.doAnswer(invocation -> {
            mockLetter.updateDelete(true);
            return null;
        }).when(mapLetterRepository).softDelete(letterId);

        // when
        mapLetterService.deleteMapLetter(List.of(letterId), userId);

        // then
        assertTrue(mockLetter.isDeleted(), "isDeleted가 true로 변경되어야 합니다.");
        verify(mapLetterRepository, Mockito.times(1)).softDelete(letterId);
    }

    @Test
    @DisplayName("PRIVATE 편지 삭제 권한이 없을 경우 CommonForbiddenException 예외를 발생시킨다.")
    void deletePrivateMapLetterWithoutPermissionTest() {
        // given
        Long letterId = 1L;
        Long userId = 3L; // 작성자가 아닌 사용자 ID
        Long userId2 = 2L; // 작성자
        Long targetUserId = 7L;

        MapLetter mockLetter = MapLetter.builder().id(letterId).title("테스트 편지").content("삭제 테스트").createUserId(userId2)
                .type(MapLetterType.PRIVATE).targetUserId(targetUserId).build();

        when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when & then
        CommonForbiddenException exception = assertThrows(CommonForbiddenException.class,
                () -> mapLetterService.deleteMapLetter(List.of(letterId), userId));

        assertEquals("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.", exception.getMessage());

        verify(mapLetterRepository, Mockito.times(0)).softDelete(Mockito.anyLong());
    }

    @Test
    @DisplayName("보낸 지도 편지 조회에 성공한다.")
    void findSentMapLettersTest() {
        // given
        Long userId = 1L;

        // Mock 데이터 생성
        List<FindSentMapLetter> mockFindSentMapLetters = List.of(
                new FindSentMapLetter() {
                    @Override
                    public Long getLetterId() {
                        return 1L;
                    }

                    @Override
                    public String getTitle() {
                        return "Title1";
                    }

                    @Override
                    public String getDescription() {
                        return "장소 설명";
                    }

                    @Override
                    public String getLabel() {
                        return "www.label.com";
                    }

                    @Override
                    public Long getTargetUser() {
                        return null; // PUBLIC 편지의 경우 null
                    }

                    @Override
                    public String getType() {
                        return "PUBLIC";
                    }

                    @Override
                    public LocalDateTime getCreatedAt() {
                        return LocalDateTime.now();
                    }

                    @Override
                    public Long getSourceLetterId() {
                        return null; // PUBLIC 편지의 경우 null
                    }
                },
                new FindSentMapLetter() {
                    @Override
                    public Long getLetterId() {
                        return 2L;
                    }

                    @Override
                    public String getTitle() {
                        return "Title2";
                    }

                    @Override
                    public String getDescription() {
                        return "장소 설명";
                    }

                    @Override
                    public String getLabel() {
                        return "www.label.com";
                    }

                    @Override
                    public Long getTargetUser() {
                        return null; // PUBLIC 편지의 경우 null
                    }

                    @Override
                    public String getType() {
                        return "PUBLIC";
                    }

                    @Override
                    public LocalDateTime getCreatedAt() {
                        return LocalDateTime.now();
                    }

                    @Override
                    public Long getSourceLetterId() {
                        return null; // PUBLIC 편지의 경우 null
                    }
                }
        );

        Page<FindSentMapLetter> mockPage = new PageImpl<>(mockFindSentMapLetters, PageRequest.of(0, 9),
                mockFindSentMapLetters.size());

        when(mapLetterRepository.findSentLettersByUserId(Mockito.eq(userId), Mockito.eq(PageRequest.of(0, 9))))
                .thenReturn(mockPage);

        // when
        Page<FindMapLetterResponseDTO> result = mapLetterService.findSentMapLetters(1, 9, userId);

        // then
        assertEquals(2, result.getTotalElements());
        assertEquals("Title1", result.getContent().get(0).title());
        assertEquals("Title2", result.getContent().get(1).title());

        verify(mapLetterRepository, Mockito.times(1))
                .findSentLettersByUserId(Mockito.eq(userId), Mockito.eq(PageRequest.of(0, 9)));
    }

    @Test
    @DisplayName("받은 지도 편지 조회에 성공한다.")
    void findReceivedMapLettersTest() {
        // given
        Long userId = 1L;

        // Mock 데이터 생성
        List<FindReceivedMapLetterDTO> mockReceivedLetters = List.of(
                new FindReceivedMapLetterDTO() {
                    @Override
                    public Long getLetterId() {
                        return 1L;
                    }

                    @Override
                    public String getTitle() {
                        return "Re: Original Title";
                    }

                    @Override
                    public String getDescription() {
                        return null; // REPLY 편지이므로 null
                    }

                    @Override
                    public BigDecimal getLatitude() {
                        return null; // REPLY 편지이므로 null
                    }

                    @Override
                    public BigDecimal getLongitude() {
                        return null; // REPLY 편지이므로 null
                    }

                    @Override
                    public String getLabel() {
                        return "www.label1.com";
                    }

                    @Override
                    public Long getSourceLetterId() {
                        return 10L; // 원본 편지 ID
                    }

                    @Override
                    public String getType() {
                        return "REPLY";
                    }

                    @Override
                    public LocalDateTime getCreatedAt() {
                        return LocalDateTime.now();
                    }

                    @Override
                    public Long getSenderId() {
                        return 2L;
                    }
                },
                new FindReceivedMapLetterDTO() {
                    @Override
                    public Long getLetterId() {
                        return 2L;
                    }

                    @Override
                    public String getTitle() {
                        return "Target Title";
                    }

                    @Override
                    public String getDescription() {
                        return "Target description";
                    }

                    @Override
                    public BigDecimal getLatitude() {
                        return new BigDecimal("37.5665");
                    }

                    @Override
                    public BigDecimal getLongitude() {
                        return new BigDecimal("126.9780");
                    }

                    @Override
                    public String getLabel() {
                        return "www.label2.com";
                    }

                    @Override
                    public Long getSourceLetterId() {
                        return null; // TARGET 편지이므로 null
                    }

                    @Override
                    public String getType() {
                        return "TARGET";
                    }

                    @Override
                    public LocalDateTime getCreatedAt() {
                        return LocalDateTime.now();
                    }

                    @Override
                    public Long getSenderId() {
                        return 3L;
                    }
                }
        );

        Page<FindReceivedMapLetterDTO> mockPage = new PageImpl<>(mockReceivedLetters, PageRequest.of(0, 10),
                mockReceivedLetters.size());

        when(mapLetterRepository.findActiveReceivedMapLettersByUserId(Mockito.eq(userId),
                any(PageRequest.class)))
                .thenReturn(mockPage);

        when(userService.getNicknameById(3L)).thenReturn("SenderUser2");
        when(userService.getProfileImageUrlById(3L)).thenReturn("www.profile2.com");

        // when
        Page<FindReceivedMapLetterResponseDTO> result = mapLetterService.findReceivedMapLetters(1, 10, userId);

        // then
        assertEquals(2, result.getTotalElements());

        // 첫 번째 편지 검증
        FindReceivedMapLetterResponseDTO firstLetter = result.getContent().get(0);
        assertEquals("Re: Original Title", firstLetter.title());
        assertNull(firstLetter.description()); // REPLY 편지
        assertNull(firstLetter.longitude());

        // 두 번째 편지 검증
        FindReceivedMapLetterResponseDTO secondLetter = result.getContent().get(1);
        assertEquals("Target Title", secondLetter.title());
        assertEquals("Target description", secondLetter.description());
        assertEquals(new BigDecimal("37.5665"), secondLetter.latitude());
        assertEquals(new BigDecimal("126.9780"), secondLetter.longitude());
        assertEquals("www.label2.com", secondLetter.label());
        assertEquals("SenderUser2", secondLetter.senderNickname());
        assertEquals("www.profile2.com", secondLetter.senderProfileImg());

        verify(mapLetterRepository, Mockito.times(1))
                .findActiveReceivedMapLettersByUserId(Mockito.eq(userId), any(PageRequest.class));
    }

    @Test
    @DisplayName("반경 500m 이내 편지 조회에 성공한다")
    void findNearByMapLetters() {
        //given
        BigDecimal latitude = new BigDecimal("37.5665");
        BigDecimal longitude = new BigDecimal("126.9780");
        Long userId = 1L;

        //편지 mock 데이터
        MapLetterAndDistance letter1 = mock(MapLetterAndDistance.class);
        when(letter1.getLetterId()).thenReturn(1L);
        when(letter1.getLatitude()).thenReturn(latitude);
        when(letter1.getLongitude()).thenReturn(longitude);
        when(letter1.getTitle()).thenReturn("Letter 1");
        when(letter1.getDescription()).thenReturn("Description 1");
        when(letter1.getDistance()).thenReturn(new BigDecimal("10.0"));
        when(letter1.getCreateUserId()).thenReturn(2L);
        when(letter1.getLabel()).thenReturn("Label1");

        MapLetterAndDistance letter2 = mock(MapLetterAndDistance.class);
        when(letter2.getLetterId()).thenReturn(2L);
        when(letter2.getLatitude()).thenReturn(latitude);
        when(letter2.getLongitude()).thenReturn(longitude);
        when(letter2.getTitle()).thenReturn("Letter 2");
        when(letter2.getDescription()).thenReturn("Description 2");
        when(letter2.getDistance()).thenReturn(new BigDecimal("15.0"));
        when(letter2.getCreateUserId()).thenReturn(3L);
        when(letter2.getLabel()).thenReturn("Label2");

        List<MapLetterAndDistance> mockLetters = List.of(letter1, letter2);

        when(mapLetterRepository.findLettersByUserLocation(latitude, longitude, userId))
                .thenReturn(mockLetters);
        when(userService.getNicknameById(2L)).thenReturn("SenderUser2");
        when(userService.getNicknameById(3L)).thenReturn("SenderUser3");

        //when
        List<FindNearbyLettersResponseDTO> result = mapLetterService.findNearByMapLetters(latitude, longitude, userId);

        //then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Letter 1", result.get(0).title());
        assertEquals("SenderUser2", result.get(0).createUserNickname());
        assertEquals("Letter 2", result.get(1).title());

        verify(mapLetterRepository).findLettersByUserLocation(latitude, longitude, userId);
        verify(userService).getNicknameById(2L);
        verify(userService).getNicknameById(3L);
    }

    @Test
    @DisplayName("답장 편지 생성에 성공한다.")
    void createReplyLetter() {
        ListOperations<String, String> listOperations = mock(ListOperations.class);
        when(redisTemplate.opsForList()).thenReturn(listOperations);
        when(listOperations.size(anyString())).thenReturn(0L);

        // given
        Long userId = 1L;
        Long sourceLetterId = 1L;
        Long replyLetterId = 2L;

        CreateReplyMapLetterRequestDTO createReplyMapLetter = new CreateReplyMapLetterRequestDTO(
                sourceLetterId, "content", "font1", "paper1", "label1");

        MapLetter mockSourceLetter = mock(MapLetter.class);
        when(mockSourceLetter.getCreateUserId()).thenReturn(2L);

        ReplyMapLetter mockReplyLetter = mock(ReplyMapLetter.class);
        when(mockReplyLetter.getReplyLetterId()).thenReturn(replyLetterId);
        when(mockReplyLetter.getSourceLetterId()).thenReturn(sourceLetterId);
        when(mockReplyLetter.getLabel()).thenReturn("label1");

        when(replyMapLetterRepository.findByLetterIdAndUserId(sourceLetterId, userId)).thenReturn(false); //답장을 하지 않은 상태
        when(mapLetterRepository.findSourceMapLetterById(sourceLetterId)).thenReturn(mockSourceLetter);
        when(replyMapLetterRepository.save(any(ReplyMapLetter.class))).thenReturn(mockReplyLetter);
        when(mapLetterRepository.findById(sourceLetterId)).thenReturn(mockSourceLetter);

        // when
        ReplyMapLetter result = mapLetterService.createReplyMapLetter(createReplyMapLetter, userId);

        // then
        assertNotNull(result);
        assertEquals(replyLetterId, result.getReplyLetterId());
        assertEquals("label1", result.getLabel());
        assertEquals(sourceLetterId, result.getSourceLetterId());

        verify(replyMapLetterRepository).findByLetterIdAndUserId(sourceLetterId, userId);
        verify(mapLetterRepository).findSourceMapLetterById(sourceLetterId);
        verify(replyMapLetterRepository).save(any(ReplyMapLetter.class));
        verify(mapLetterRepository).findById(sourceLetterId);
    }

    @Test
    @DisplayName("이미 답장을 한 편지에는 답장 편지 생성에 실패한다.")
    void alreadyRepliedLetter() {
        // given
        Long userId = 1L;
        Long sourceLetterId = 1L;

        CreateReplyMapLetterRequestDTO createReplyMapLetter = new CreateReplyMapLetterRequestDTO(
                sourceLetterId, "content", "font1", "paper1", "label1");

        when(replyMapLetterRepository.findByLetterIdAndUserId(sourceLetterId, userId)).thenReturn(true); // 답장을 한 상태

        // when && then
        Exception exception = assertThrows(LetterAlreadyReplyException.class,
                () -> mapLetterService.createReplyMapLetter(createReplyMapLetter, userId));
        assertEquals("해당 편지에 이미 답장을 했습니다.", exception.getMessage());

        verify(replyMapLetterRepository).findByLetterIdAndUserId(sourceLetterId, userId);
    }
}

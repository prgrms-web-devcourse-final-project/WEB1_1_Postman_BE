package postman.bottler.mapletter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindMapLetterResponseDTO;

@ExtendWith(MockitoExtension.class)
class MapLetterServiceTest {
    @InjectMocks
    private MapLetterService mapLetterService;
    @Mock
    private MapLetterRepository mapLetterRepository;

    @BeforeEach
    void resetMocks() {
        Mockito.reset(mapLetterRepository); // Mock 상태 초기화
    }

    @Test
    @DisplayName("퍼블릭 편지 생성에 성공한다.")
    void createPublicMapLetterTest() {
        //given
        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO(
                "Test Title",
                "TestContent",
                "장소 설명",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com"
        );
        Long userId = 1L;
        MapLetter expectedMapLetter = MapLetter.createPublicMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.save(Mockito.any(MapLetter.class))).thenReturn(expectedMapLetter);

        //when
        MapLetter actualMapLetter = mapLetterService.createPublicMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(), actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(), actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(), actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(), actualMapLetter.getLongitude());
        assertEquals(expectedMapLetter.getDescription(), actualMapLetter.getDescription());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

    @Test
    @DisplayName("타겟 편지 생성에 성공한다.")
    void createTargetLetterTest() {
        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
                "Test Title",
                "TestContent",
                "장소 설명",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com",
                2L //임시 타겟
        );

        Long userId = 1L;
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.save(Mockito.any(MapLetter.class))).thenReturn(expectedMapLetter);

        //when
        MapLetter actualMapLetter = mapLetterService.createTargetMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(), actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(), actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(), actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(), actualMapLetter.getLongitude());
        assertEquals(expectedMapLetter.getTargetUserId(), actualMapLetter.getTargetUserId());
        assertEquals(expectedMapLetter.getDescription(), actualMapLetter.getDescription());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

//    @Test
//    @DisplayName("편지가 PUBLIC일 경우 편지 상세 조회에 성공한다.")
//    void findPublicMapLetterTest() {
//        // given
//        Long letterId = 1L;
//        Long userId = 2L;
//
//        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO(
//                "퍼블릭 편지 상세 조회 테스트",
//                "편지 내용",
//                "장소 설명",
//                new BigDecimal("37.5665"),
//                new BigDecimal("127.23456"),
//                "맑은고딕",
//                "www.paper.com",
//                "www.label.com"
//        );
//        MapLetter publicLetter = MapLetter.createPublicMapLetter(requestDTO, userId);
//
//        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(publicLetter);
//
//        // when
//        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(letterId, userId);
//
//        // then
//        assertNotNull(response);
//        assertEquals("퍼블릭 편지 상세 조회 테스트", response.title());
//        assertEquals("편지 내용", response.content());
//        assertEquals("맑은고딕", response.font());
//        assertEquals("장소 설명",response.description());
//        assertEquals("www.paper.com", response.paper());
//        assertEquals("www.label.com", response.label());
//        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
//    }

//    @Test
//    @DisplayName("편지가 TARGET이고, TARGET ID와 USER ID가 동일하면 편지 상세 조회에 성공한다.")
//    void findTargetMapLetterTest() {
//        // given
//        Long letterId = 1L;
//        Long userId = 2L;
//        Long targetUserId = 7L;
//
//        //given
//        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
//                "프라이빗 편지 편지 상세 조회 테스트",
//                "편지 내용",
//                "장소 설명",
//                new BigDecimal("37.5665"),
//                new BigDecimal("127.23456"),
//                "맑은고딕",
//                "www.paper.com",
//                "www.label.com",
//                targetUserId
//        );
//        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);
//
//        expectedMapLetter = MapLetter.builder()
//                .id(letterId) // id를 명시적으로 설정
//                .title(expectedMapLetter.getTitle())
//                .content(expectedMapLetter.getContent())
//                .description(expectedMapLetter.getDescription())
//                .latitude(expectedMapLetter.getLatitude())
//                .longitude(expectedMapLetter.getLongitude())
//                .font(expectedMapLetter.getFont())
//                .paper(expectedMapLetter.getPaper())
//                .label(expectedMapLetter.getLabel())
//                .type(expectedMapLetter.getType())
//                .targetUserId(expectedMapLetter.getTargetUserId())
//                .createUserId(expectedMapLetter.getCreateUserId())
//                .createdAt(expectedMapLetter.getCreatedAt())
//                .updatedAt(expectedMapLetter.getUpdatedAt())
//                .isDeleted(expectedMapLetter.isDeleted())
//                .isBlocked(expectedMapLetter.isBlocked())
//                .build();
//
//        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);
//
//        // when
//        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(expectedMapLetter.getId(), targetUserId);
//
//        // then
//        assertNotNull(response);
//        assertEquals(MapLetterType.PRIVATE, expectedMapLetter.getType()); //타입이 PRIVATE인지 조회
//        assertEquals(targetUserId, expectedMapLetter.getTargetUserId()); //타겟이 맞는지 조회
//        assertEquals("프라이빗 편지 편지 상세 조회 테스트", response.title());
//        assertEquals("장소 설명", response.description());
//        assertEquals("편지 내용", response.content());
//        assertEquals("맑은고딕", response.font());
//        assertEquals("www.paper.com", response.paper());
//        assertEquals("www.label.com", response.label());
//        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
//    }

//    @Test
//    @DisplayName("편지가 TARGET이고, CREATE USER ID와 USER ID가 동일하면 편지 상세 조회에 성공한다.")
//    void findCreateUserTargetMapLetterTest() {
//        // given
//        Long letterId = 1L;
//        Long userId = 2L;
//        Long targetUserId = 7L;
//
//        //given
//        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
//                "프라이빗 편지 편지 상세 조회 테스트",
//                "편지 내용",
//                "장소 설명",
//                new BigDecimal("37.5665"),
//                new BigDecimal("127.23456"),
//                "맑은고딕",
//                "www.paper.com",
//                "www.label.com",
//                targetUserId
//        );
//        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);
//
//        expectedMapLetter = MapLetter.builder()
//                .id(letterId) // id를 명시적으로 설정
//                .title(expectedMapLetter.getTitle())
//                .content(expectedMapLetter.getContent())
//                .description(expectedMapLetter.getDescription())
//                .latitude(expectedMapLetter.getLatitude())
//                .longitude(expectedMapLetter.getLongitude())
//                .font(expectedMapLetter.getFont())
//                .paper(expectedMapLetter.getPaper())
//                .label(expectedMapLetter.getLabel())
//                .type(expectedMapLetter.getType())
//                .targetUserId(expectedMapLetter.getTargetUserId())
//                .createUserId(expectedMapLetter.getCreateUserId())
//                .createdAt(expectedMapLetter.getCreatedAt())
//                .updatedAt(expectedMapLetter.getUpdatedAt())
//                .isDeleted(expectedMapLetter.isDeleted())
//                .isBlocked(expectedMapLetter.isBlocked())
//                .build();
//
//        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);
//
//        // when
//        OneLetterResponseDTO response = mapLetterService.findOneMapLetter(expectedMapLetter.getId(), userId);
//
//        // then
//        assertNotNull(response);
//        assertEquals(MapLetterType.PRIVATE, expectedMapLetter.getType()); //타입이 PRIVATE인지 조회
//        assertEquals(userId, expectedMapLetter.getCreateUserId());
//        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
//    }

//    @Test
//    @DisplayName("편지가 TARGET이고 TARGET ID와 USER ID가 일치하지 않을 경우 편지 상세 조회에 실패한다.")
//    void findTargetMapLetterUnauthorized() {
//        // given
//        Long letterId = 1L;
//        Long userId = 2L;
//        Long targetUserId = 7L;
//        Long unAuthorizedUserId = 10L;
//
//        //given
//        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
//                "프라이빗 편지 편지 상세 조회 테스트",
//                "편지 내용",
//                "장소 설명",
//                new BigDecimal("37.5665"),
//                new BigDecimal("127.23456"),
//                "맑은고딕",
//                "www.paper.com",
//                "www.label.com",
//                targetUserId
//        );
//        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);
//
//        expectedMapLetter = MapLetter.builder()
//                .id(letterId) // id를 명시적으로 설정
//                .title(expectedMapLetter.getTitle())
//                .content(expectedMapLetter.getContent())
//                .description(expectedMapLetter.getDescription())
//                .latitude(expectedMapLetter.getLatitude())
//                .longitude(expectedMapLetter.getLongitude())
//                .font(expectedMapLetter.getFont())
//                .paper(expectedMapLetter.getPaper())
//                .label(expectedMapLetter.getLabel())
//                .type(expectedMapLetter.getType())
//                .targetUserId(expectedMapLetter.getTargetUserId())
//                .createUserId(expectedMapLetter.getCreateUserId())
//                .createdAt(expectedMapLetter.getCreatedAt())
//                .updatedAt(expectedMapLetter.getUpdatedAt())
//                .isDeleted(expectedMapLetter.isDeleted())
//                .isBlocked(expectedMapLetter.isBlocked())
//                .build();
//
//        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);
//
//        // when & then
//        Exception exception = assertThrows(CommonForbiddenException.class, () ->
//                mapLetterService.findOneMapLetter(letterId, unAuthorizedUserId)
//        );
//
//        assertEquals("편지를 볼 수 있는 권한이 없습니다.", exception.getMessage());
//        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
//    }

    @Test
    @DisplayName("PUBLIC 편지 삭제에 성공하고 isDeleted 상태가 true로 변경된다.")
    void deletePublicMapLetterAndVerifyIsDeletedTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(userId)
                .type(MapLetterType.PUBLIC)
                .isDeleted(false) // 초기 상태는 false
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        Mockito.doAnswer(invocation -> {
            mockLetter.updateDelete(true);
            return null;
        }).when(mapLetterRepository).softDelete(letterId);

        // when
        mapLetterService.deleteMapLetter(List.of(letterId), userId);

        // then
        assertTrue(mockLetter.isDeleted(), "isDeleted가 true로 변경되어야 합니다.");
        Mockito.verify(mapLetterRepository, Mockito.times(1)).softDelete(letterId);
    }

    @Test
    @DisplayName("PUBLIC 편지 삭제 권한이 없을 경우 CommonForbiddenException 예외를 발생시킨다.")
    void deletePublicMapLetterWithoutPermissionTest() {
        // given
        Long letterId = 1L;
        Long userId = 3L; // 작성자가 아닌 사용자 ID
        Long userId2 = 2L; // 작성자

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(userId2)
                .type(MapLetterType.PUBLIC)
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when & then
        CommonForbiddenException exception = assertThrows(CommonForbiddenException.class,
                () -> mapLetterService.deleteMapLetter(List.of(letterId), userId));

        assertEquals("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.", exception.getMessage());

        Mockito.verify(mapLetterRepository, Mockito.times(0)).softDelete(Mockito.anyLong());
    }

    @Test
    @DisplayName("TARGET 편지 삭제에 성공하고, isDeleted 상태가 true로 변경된다.")
    void deleteTargetMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(userId)
                .type(MapLetterType.PRIVATE)
                .isDeleted(false)
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        Mockito.doAnswer(invocation -> {
            mockLetter.updateDelete(true);
            return null;
        }).when(mapLetterRepository).softDelete(letterId);

        // when
        mapLetterService.deleteMapLetter(List.of(letterId), userId);

        // then
        assertTrue(mockLetter.isDeleted(), "isDeleted가 true로 변경되어야 합니다.");
        Mockito.verify(mapLetterRepository, Mockito.times(1)).softDelete(letterId);
    }

    @Test
    @DisplayName("PRIVATE 편지 삭제 권한이 없을 경우 CommonForbiddenException 예외를 발생시킨다.")
    void deletePrivateMapLetterWithoutPermissionTest() {
        // given
        Long letterId = 1L;
        Long userId = 3L; // 작성자가 아닌 사용자 ID
        Long userId2 = 2L; // 작성자
        Long targetUserId = 7L;

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(userId2)
                .type(MapLetterType.PRIVATE)
                .targetUserId(targetUserId)
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when & then
        CommonForbiddenException exception = assertThrows(CommonForbiddenException.class,
                () -> mapLetterService.deleteMapLetter(List.of(letterId), userId));

        assertEquals("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.", exception.getMessage());

        Mockito.verify(mapLetterRepository, Mockito.times(0)).softDelete(Mockito.anyLong());
    }

    @Test
    @DisplayName("보낸 지도 편지 조회에 성공한다.")
    void findSentMapLettersTest() {
        //given
        Long userId = 1L;
        Long userId2 = 2L;

        List<MapLetter> mockMapLetters = List.of(
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title1", "content1",
                        "장소 설명", new BigDecimal("37.566"), new BigDecimal("127.34567"), "프리텐다드",
                        "www.paper.com", "www.label.com"), userId //조회 될 편지
                ),
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title2", "content2",
                        "장소 설명", new BigDecimal("37.566"), new BigDecimal("127.3456"), "맑은고딕",
                        "www.paper.com", "www.label.com"), userId //조회 될 편지
                ),
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title3", "content2",
                        "장소 설명", new BigDecimal("37.566"), new BigDecimal("127.3456"), "맑은고딕",
                        "www.paper.com", "www.label.com"), userId2 //다른 유저가 작성한 편지
                ),
                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
                        "Target Title 1", "content 1", "장소 설명", new BigDecimal("12.1234"),
                        new BigDecimal("127.12345"), "맑은 고딕", "www.paper.com", "www.label.com",
                        2L), userId //조회 될 편지
                ),
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title4", "content1",
                        "장소 설명", new BigDecimal("37.566"), new BigDecimal("127.34567"), "프리텐다드",
                        "www.paper.com", "www.label.com"), userId //삭제 될 편지
                ),
                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
                        "Target Title 2", "content 2", "장소 설명", new BigDecimal("12.1234"),
                        new BigDecimal("127.12345"), "굴림체", "www.paper.com", "www.label4.com",
                        2L), userId //조회 될 편지
                ),
                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
                        "Target Title 3", "content 3", "장소 설명", new BigDecimal("12.1234"),
                        new BigDecimal("127.12345"), "굴림체", "www.paper.com", "www.label4.com",
                        2L), userId2 //다른 유저가 작성한 편지
                )
        );

        mockMapLetters.get(4).updateDelete(true);

        List<MapLetter> filteredMapLetters = mockMapLetters.stream()
                .filter(letter -> letter.getCreateUserId().equals(userId))
                .filter(letter -> !letter.isDeleted())
                .toList();

        Mockito.when(mapLetterRepository.findActiveByCreateUserId(userId)).thenReturn(filteredMapLetters);

        //when
        List<FindMapLetterResponseDTO> result = mapLetterService.findSentMapLetters(userId);

        //then
        assertEquals(4, result.size());
        assertEquals("Title1", result.get(0).title());
        assertEquals("Title2", result.get(1).title());
        assertEquals("장소 설명", result.get(3).description());
        assertEquals("www.label4.com", result.get(3).label());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).findActiveByCreateUserId(userId);

    }

//    @Test
//    @DisplayName("받은 지도 편지 조회에 성공한다.")
//    void findReceivedMapLettersTest() {
//        //given
//        Long userId = 1L;
//        Long userId2 = 2L;
//
//        List<MapLetter> mockMapLetters = List.of(
//                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title1", "content1",
//                        "장소 설명",new BigDecimal("37.566"), new BigDecimal("127.34567"), "프리텐다드",
//                        "www.paper.com", "www.label.com"), userId
//                ),
//                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title2", "content2",
//                        "장소 설명",new BigDecimal("37.566"), new BigDecimal("127.3456"), "맑은고딕",
//                        "www.paper.com", "www.label.com"), userId
//                ),
//                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title3", "content2",
//                        "장소 설명",new BigDecimal("37.566"), new BigDecimal("127.3456"), "맑은고딕",
//                        "www.paper.com", "www.label.com"), userId2
//                ),
//                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
//                        "Target Title 1", "content 1", "장소 설명",new BigDecimal("12.1234"),
//                        new BigDecimal("127.12345"), "맑은 고딕", "www.paper.com","www.label.com",
//                        userId),userId //조회 될 편지
//                ),
//                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
//                        "Target Title 2", "content 2", "장소 설명",new BigDecimal("12.1234"),
//                        new BigDecimal("127.12345"), "굴림체", "www.paper.com","www.label4.com",
//                        userId2),userId //다른 타겟에게 간 편지
//                ),
//                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
//                        "Target Title 3", "content 2", "장소 설명",new BigDecimal("12.1234"),
//                        new BigDecimal("127.12345"), "굴림체", "www.paper.com","www.label4.com",
//                        userId),userId //삭제할편지
//                ),
//                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
//                        "Target Title 4", "content 3", "장소 설명",new BigDecimal("12.1234"),
//                        new BigDecimal("127.12345"), "굴림체", "www.paper.com","www.label4.com",
//                        userId),userId2 //조회 될 편지
//                )
//        );
//
//        mockMapLetters.get(5).updateDelete(true);
//
//        List<MapLetter> filteredMapLetters = mockMapLetters.stream()
//                .filter(letter -> letter.getType().equals(MapLetterType.PRIVATE) && letter.getTargetUserId().equals(userId))
//                .filter(letter -> !letter.isDeleted())
//                .toList();
//
//        Mockito.when(mapLetterRepository.findActiveByTargetUserId(userId)).thenReturn(filteredMapLetters);
//
//
//        //when
//        List<FindReceivedMapLetterResponseDTO> result=mapLetterService.findReceivedMapLetters(userId);
//
//        //then
//        assertEquals(2, result.size());
//        assertEquals("Target Title 1", result.get(0).title());
//        assertEquals("Target Title 4", result.get(1).title());
//        assertEquals("www.label4.com", result.get(1).label());
//
//        Mockito.verify(mapLetterRepository, Mockito.times(1)).findActiveByTargetUserId(userId);
//    }

//    @Test
//    @DisplayName("주변에 있는 편지 조회에 성공한다.")
//    public void findNearByMapLettersTest() {
//        // Given
//        BigDecimal latitude = BigDecimal.valueOf(37.5665); // 현재 사용자 위치
//        BigDecimal longitude = BigDecimal.valueOf(126.9780);
//        Long userId = 1L; // 현재 사용자 ID
//        Long targetUserId = 2L;
//        Long targetUserId2=7L;
//
//        List<MapLetterAndDistance> mockMapLetters = List.of(
//                createMockMapLetterAndDistance(1L, "퍼블릭 편지 1", latitude, longitude, "맑은고딕", userId, null, new BigDecimal("300.0")),
//                createMockMapLetterAndDistance(3L, "타겟 편지 1", latitude, longitude, "굴림체", userId, targetUserId, new BigDecimal("200.0"))
//        );
//
//        Mockito.when(mapLetterRepository.findLettersByUserLocation(latitude, longitude, userId))
//                .thenReturn(mockMapLetters);
//
//        // When
//        List<FindNearbyLettersResponseDTO> result = mapLetterService.findNearByMapLetters(latitude, longitude, userId);
//
//        // Then
//        assertEquals(2, result.size());
//        assertEquals("퍼블릭 편지 1", result.get(0).title());
//        assertEquals("타겟 편지 1", result.get(1).title());
//    }
//
//    private FindNearbyLettersResponseDTO createMockMapLetterAndDistance(
//            Long letterId,
//            String title,
//            BigDecimal latitude,
//            BigDecimal longitude,
//            String label,
//            Long createUserId,
//            Long targetUserId,
//            BigDecimal distance
//    ) {
//        return new FindNearbyLettersResponseDTO() {
//            @Override
//            public Long getLetterId() {
//                return letterId;
//            }
//
//            @Override
//            public BigDecimal getLatitude() {
//                return latitude;
//            }
//
//            @Override
//            public BigDecimal getLongitude() {
//                return longitude;
//            }
//
//            @Override
//            public String getTitle() {
//                return title;
//            }
//
//            @Override
//            public LocalDateTime getCreatedAt() {
//                return LocalDateTime.now();
//            }
//
//            @Override
//            public BigDecimal getDistance() {
//                return distance;
//            }
//
//            @Override
//            public Long getTargetUserId() {
//                return targetUserId;
//            }
//
//            @Override
//            public Long getCreateUserId() {
//                return createUserId;
//            }
//
//            @Override
//            public String getLabel() {
//                return label;
//            }
//        };
//    }
}

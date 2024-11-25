package postman.bottler.mapletter.service;

import jakarta.validation.constraints.NotBlank;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindSentMapLetter;
import postman.bottler.mapletter.dto.response.OneLetterResponse;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapLetterServiceTest {
    @Autowired
    private MapLetterService mapLetterService;
    @MockBean
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

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

    @Test
    @DisplayName("타겟 편지 생성에 성공한다.")
    void createTargetLetterTest() {
        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
                "Test Title",
                "TestContent",
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

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

    @Test
    @DisplayName("편지가 PUBLIC일 경우 편지 상세 조회에 성공한다.")
    void findPublicMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        CreatePublicMapLetterRequestDTO requestDTO = new CreatePublicMapLetterRequestDTO(
                "퍼블릭 편지 상세 조회 테스트",
                "편지 내용",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com"
        );
        MapLetter publicLetter = MapLetter.createPublicMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(publicLetter);

        // when
        OneLetterResponse response = mapLetterService.findOneMepLetter(letterId, userId);

        // then
        assertNotNull(response);
        assertEquals("퍼블릭 편지 상세 조회 테스트", response.title());
        assertEquals("편지 내용", response.content());
        assertEquals("맑은고딕", response.font());
        assertEquals("www.paper.com", response.paper());
        assertEquals("www.label.com", response.label());
        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("편지가 TARGET이고, TARGET ID와 USER ID가 동일하면 편지 상세 조회에 성공한다.")
    void findTargetMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;
        Long targetUserId = 7L;

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
                "프라이빗 편지 편지 상세 조회 테스트",
                "편지 내용",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com",
                targetUserId
        );
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);

        expectedMapLetter = MapLetter.builder()
                .id(letterId) // id를 명시적으로 설정
                .title(expectedMapLetter.getTitle())
                .content(expectedMapLetter.getContent())
                .latitude(expectedMapLetter.getLatitude())
                .longitude(expectedMapLetter.getLongitude())
                .font(expectedMapLetter.getFont())
                .paper(expectedMapLetter.getPaper())
                .label(expectedMapLetter.getLabel())
                .type(expectedMapLetter.getType())
                .targetUserId(expectedMapLetter.getTargetUserId())
                .createUserId(expectedMapLetter.getCreateUserId())
                .createdAt(expectedMapLetter.getCreatedAt())
                .updatedAt(expectedMapLetter.getUpdatedAt())
                .isDeleted(expectedMapLetter.isDeleted())
                .isBlocked(expectedMapLetter.isBlocked())
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);

        // when
        OneLetterResponse response = mapLetterService.findOneMepLetter(expectedMapLetter.getId(), targetUserId);

        // then
        assertNotNull(response);
        assertEquals(MapLetterType.PRIVATE, expectedMapLetter.getType()); //타입이 PRIVATE인지 조회
        assertEquals(targetUserId, expectedMapLetter.getTargetUserId()); //타겟이 맞는지 조회
        assertEquals("프라이빗 편지 편지 상세 조회 테스트", response.title());
        assertEquals("편지 내용", response.content());
        assertEquals("맑은고딕", response.font());
        assertEquals("www.paper.com", response.paper());
        assertEquals("www.label.com", response.label());
        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("편지가 TARGET이고 TARGET ID와 USER ID가 일치하지 않을 경우 편지 상세 조회에 실패한다.")
    void findTargetMapLetterUnauthorized() {
        // given
        Long letterId = 1L;
        Long userId = 2L;
        Long targetUserId = 7L;
        Long unAuthorizedUserId = 10L;

        //given
        CreateTargetMapLetterRequestDTO requestDTO = new CreateTargetMapLetterRequestDTO(
                "프라이빗 편지 편지 상세 조회 테스트",
                "편지 내용",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com",
                targetUserId
        );
        MapLetter expectedMapLetter = MapLetter.createTargetMapLetter(requestDTO, userId);

        expectedMapLetter = MapLetter.builder()
                .id(letterId) // id를 명시적으로 설정
                .title(expectedMapLetter.getTitle())
                .content(expectedMapLetter.getContent())
                .latitude(expectedMapLetter.getLatitude())
                .longitude(expectedMapLetter.getLongitude())
                .font(expectedMapLetter.getFont())
                .paper(expectedMapLetter.getPaper())
                .label(expectedMapLetter.getLabel())
                .type(expectedMapLetter.getType())
                .targetUserId(expectedMapLetter.getTargetUserId())
                .createUserId(expectedMapLetter.getCreateUserId())
                .createdAt(expectedMapLetter.getCreatedAt())
                .updatedAt(expectedMapLetter.getUpdatedAt())
                .isDeleted(expectedMapLetter.isDeleted())
                .isBlocked(expectedMapLetter.isBlocked())
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(expectedMapLetter);

        // when & then
        Exception exception = assertThrows(CommonForbiddenException.class, () ->
                mapLetterService.findOneMepLetter(letterId, unAuthorizedUserId)
        );

        assertEquals("편지를 볼 수 있는 권한이 없습니다.", exception.getMessage());
        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }

    @Test
    @DisplayName("PUBLIC 편지 삭제에 성공한다.")
    void deletePublicMapLetterTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(userId)
                .type(MapLetterType.PUBLIC)
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when
        mapLetterService.deleteMapLetter(letterId, userId);

        // then
        Mockito.verify(mapLetterRepository, Mockito.times(1)).delete(letterId);
    }

    @Test
    @DisplayName("TARGET 편지 삭제에 성공한다.")
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
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when
        mapLetterService.deleteMapLetter(letterId, userId);

        // then
        Mockito.verify(mapLetterRepository, Mockito.times(1)).delete(letterId);
    }

    @Test
    @DisplayName("사용자가 권한이 없는 편지 삭제 시 CommonForbiddenException이 발생한다.")
    void deleteMapLetterForbiddenTest() {
        // given
        Long letterId = 1L;
        Long userId = 2L; //삭제 유저
        Long otherUserId = 3L; //편지 작성 유저

        MapLetter mockLetter = MapLetter.builder()
                .id(letterId)
                .title("테스트 편지")
                .content("삭제 테스트")
                .createUserId(otherUserId)
                .type(MapLetterType.PUBLIC)
                .build();

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(mockLetter);

        // when & then
        CommonForbiddenException exception = assertThrows(CommonForbiddenException.class, () -> {
            mapLetterService.deleteMapLetter(letterId, userId);
        });

        assertEquals("편지를 삭제 할 권한이 없습니다.", exception.getMessage());

        // 삭제 호출이 이루어지지 않아야 함
        Mockito.verify(mapLetterRepository, Mockito.never()).delete(letterId);
    }

    @Test
    @DisplayName("보낸 지도 편지 조회에 성공한다.")
    void findSentMapLettersTest() {
        //given
        Long userId = 1L;

        List<MapLetter> mockMapLetters = List.of(
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title1", "content1",
                                new BigDecimal("37.566"), new BigDecimal("127.34567"), "프리텐다드",
                                "www.paper.com", "www.label.com"), userId
                ),
                MapLetter.createPublicMapLetter(new CreatePublicMapLetterRequestDTO("Title2", "content2",
                        new BigDecimal("37.566"), new BigDecimal("127.3456"), "맑은고딕",
                        "www.paper.com", "www.label.com"), userId
                ),
                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
                        "Target Title 1", "content 1", new BigDecimal("12.1234"),
                        new BigDecimal("127.12345"), "맑은 고딕", "www.paper.com","www.label.com",
                        2L),userId
                ),
                MapLetter.createTargetMapLetter(new CreateTargetMapLetterRequestDTO(
                        "Target Title 2", "content 2", new BigDecimal("12.1234"),
                        new BigDecimal("127.12345"), "굴림체", "www.paper.com","www.label4.com",
                        2L),userId
                )
        );

        Mockito.when(mapLetterRepository.findAllByCreateUserId(userId)).thenReturn(mockMapLetters);

        //when
        List<FindSentMapLetter> result=mapLetterService.findSentMapLetters(userId);

        //then
        assertEquals(4, result.size());
        assertEquals("Title1", result.get(0).title());
        assertEquals("Title2", result.get(1).title());
        assertEquals("www.label4.com", result.get(3).label());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).findAllByCreateUserId(userId);

    }

}

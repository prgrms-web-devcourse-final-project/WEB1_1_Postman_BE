package postman.bottler.mapletter.controller;

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
import postman.bottler.mapletter.dto.response.OneLetterResponse;
import postman.bottler.mapletter.service.MapLetterRepository;
import postman.bottler.mapletter.service.MapLetterService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    void createPublicMapLetter() {
        //given
        CreatePublicMapLetterRequestDTO requestDTO=new CreatePublicMapLetterRequestDTO(
                "Test Title",
                "TestContent",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com"
        );
        Long userId=1L;
        MapLetter expectedMapLetter=MapLetter.createPublicMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.save(Mockito.any(MapLetter.class))).thenReturn(expectedMapLetter);

        //when
        MapLetter actualMapLetter=mapLetterService.createPublicMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(),actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(),actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(),actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(),actualMapLetter.getLongitude());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

    @Test
    @DisplayName("타겟 편지 생성에 성공한다.")
    void createTargetLetter(){
        //given
        CreateTargetMapLetterRequestDTO requestDTO=new CreateTargetMapLetterRequestDTO(
                "Test Title",
                "TestContent",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com",
                2L //임시 타겟
        );

        Long userId=1L;
        MapLetter expectedMapLetter=MapLetter.createTargetMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.save(Mockito.any(MapLetter.class))).thenReturn(expectedMapLetter);

        //when
        MapLetter actualMapLetter=mapLetterService.createTargetMapLetter(requestDTO, userId);

        //then
        assertEquals(expectedMapLetter.getTitle(),actualMapLetter.getTitle());
        assertEquals(expectedMapLetter.getContent(),actualMapLetter.getContent());
        assertEquals(expectedMapLetter.getLatitude(),actualMapLetter.getLatitude());
        assertEquals(expectedMapLetter.getLongitude(),actualMapLetter.getLongitude());
        assertEquals(expectedMapLetter.getTargetUserId(),actualMapLetter.getTargetUserId());

        Mockito.verify(mapLetterRepository, Mockito.times(1)).save(Mockito.any(MapLetter.class));
    }

    @Test
    @DisplayName("편지가 PUBLIC일 경우 편지 상세 조회에 성공한다.")
    void findPublicMapLetter() {
        // given
        Long letterId = 1L;
        Long userId = 2L;

        CreatePublicMapLetterRequestDTO requestDTO=new CreatePublicMapLetterRequestDTO(
                "퍼블릭 편지 상세 조회 테스트",
                "편지 내용",
                new BigDecimal("37.5665"),
                new BigDecimal("127.23456"),
                "맑은고딕",
                "www.paper.com",
                "www.label.com"
        );
        MapLetter publicLetter=MapLetter.createPublicMapLetter(requestDTO, userId);

        Mockito.when(mapLetterRepository.findById(letterId)).thenReturn(publicLetter);

        // when
        OneLetterResponse response = mapLetterService.findOneMepLetter(letterId, userId);

        // then
        assertNotNull(response);
        assertEquals("퍼블릭 편지 상세 조회 테스트", response.title());
        assertEquals("편지 내용", response.content());
        assertEquals("맑은고딕",response.font());
        assertEquals("www.paper.com", response.paper());
        assertEquals("www.label.com", response.label());
        Mockito.verify(mapLetterRepository, Mockito.times(1)).findById(letterId);
    }
}

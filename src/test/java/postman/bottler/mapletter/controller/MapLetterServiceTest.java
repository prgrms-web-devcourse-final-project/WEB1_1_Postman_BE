package postman.bottler.mapletter.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.service.MapLetterRepository;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MapLetterServiceTest {
    @Autowired
    private MapLetterService mapLetterService;
    @MockBean
    private MapLetterRepository mapLetterRepository;

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
                1,
                1
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
}

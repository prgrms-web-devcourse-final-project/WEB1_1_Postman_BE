package postman.bottler.mapletter.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.mapletter.domain.Paper;
import postman.bottler.mapletter.dto.PaperDTO;

@ExtendWith(MockitoExtension.class)
class PaperServiceTest {

    @InjectMocks
    private PaperService paperService;

    @Mock
    private PaperRepository paperRepository;

    @Test
    @DisplayName("편지지 전체 조회에 성공한다.")
    void findAllPapersTest() {
        //given
        List<Paper> mockPapers = List.of(
                Paper.builder().paperId(1L).paperUrl("www.paper1.com").build(),
                Paper.builder().paperId(2L).paperUrl("www.paper2.com").build()
        );

        List<PaperDTO> expectedPapers = mockPapers.stream()
                .map(Paper::toPaperDTO)
                .toList();

        Mockito.when(paperRepository.findAll()).thenReturn(mockPapers);

        //when
        List<PaperDTO> actualPaperDTOs = paperService.findPapers();

        //then
        assertNotNull(actualPaperDTOs);
        assertEquals(expectedPapers.size(), actualPaperDTOs.size());
        assertEquals(expectedPapers.get(0).paperUrl(), actualPaperDTOs.get(0).paperUrl());
        assertEquals(expectedPapers.get(1).paperUrl(), actualPaperDTOs.get(1).paperUrl());

        Mockito.verify(paperRepository, Mockito.times(1)).findAll();

    }

    @Test
    @DisplayName("전체 편지지 조회 시 결과가 없을 경우 빈 리스트를 반환한다.")
    void findAllPapersEmptyTest() {
        // given
        Mockito.when(paperRepository.findAll()).thenReturn(Collections.emptyList());

        // when
        List<PaperDTO> actualPaperDTOs = paperService.findPapers();

        // then
        assertNotNull(actualPaperDTOs);
        assertTrue(actualPaperDTOs.isEmpty());

        Mockito.verify(paperRepository, Mockito.times(1)).findAll();
    }
}

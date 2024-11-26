package postman.bottler.label.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import postman.bottler.label.domain.Label;
import postman.bottler.label.dto.response.LabelResponseDTO;

@SpringBootTest
class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;

    @InjectMocks
    private LabelService labelService;

    @BeforeEach
    void setUp() {
        reset(labelRepository);
    }

    @Test
    @DisplayName("주어진 이미지 URL로 라벨을 생성해서 저장한다.")
    void createLabel() {
        // given
        String imageUrl = "http://example.com/image.png";

        // when
        labelService.createLabel(imageUrl, 10);

        // then
        verify(labelRepository, times(1)).save(any(Label.class));
    }

    @Test
    @DisplayName("저장된 모든 라벨이 반환된다.")
    void findAllLabels() {
        // given
        Label label1 = Label.createLabel("http://example.com/image1.png", 10);
        Label label2 = Label.createLabel("http://example.com/image2.png", 10);
        List<Label> labels = List.of(label1, label2);

        when(labelRepository.findAllLabels()).thenReturn(labels);

        // when
        List<LabelResponseDTO> dtos = labelService.findAllLabels();

        // then
        assertNotNull(dtos, "Response DTO 리스트는 null이 될 수 없음");
        assertEquals(2, dtos.size(), "DTO 리스트 사이즈는 2");

        verify(labelRepository, times(1)).findAllLabels();
    }
}

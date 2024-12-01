package postman.bottler.label.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import postman.bottler.label.domain.Label;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.user.infra.UserJpaRepository;

@ExtendWith(MockitoExtension.class)
class LabelServiceTest {
    @Mock
    private LabelRepository labelRepository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @InjectMocks
    private LabelService labelService;

    @BeforeEach
    void setUp() {
        reset(labelRepository, userJpaRepository);
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

    @Test
    @DisplayName("특정 유저가 소유한 라벨 리스트를 반환한다.")
    void findUserLabels() {
        // given
        Long userId = 1L;
        Label label1 = Label.createLabel("http://example.com/image1.png", 10);
        Label label2 = Label.createLabel("http://example.com/image2.png", 10);
        List<Label> labels = List.of(label1, label2);

        when(labelRepository.findLabelsByUser(userId)).thenReturn(labels);

        // when
        List<LabelResponseDTO> dtos = labelService.findUserLabels(userId);

        // then
        assertNotNull(dtos, "Response DTO 리스트는 null이 될 수 없음");
        assertEquals(2, dtos.size(), "DTO 리스트 사이즈는 2");

        verify(labelRepository, times(1)).findLabelsByUser(userId);
    }
}

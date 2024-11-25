package postman.bottler.label.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import postman.bottler.label.domain.Label;

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
        labelService.createLabel(imageUrl);

        // then
        verify(labelRepository, times(1)).save(any(Label.class));
    }
}

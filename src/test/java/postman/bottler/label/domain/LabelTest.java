package postman.bottler.label.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import postman.bottler.label.dto.response.LabelResponseDTO;

@SpringBootTest
class LabelTest {
    @Test
    @DisplayName("주어진 이미지 URL로 Label 객체가 생성된다.")
    void createLabelByImageURL() {
        // given
        String imageUrl = "http://example.com/image.png";

        // when
        Label label = Label.createLabel(imageUrl);

        // then
        assertNotNull(label, "Label은 null이 될 수 없음");
        assertEquals(imageUrl, label.getImageUrl(), "주어진 url로 Label 생성");
    }

    @Test
    @DisplayName("Label 객체를 LabelResponseDTO로 변환한다.")
    void toLabelResponseDTO() {
        // given
        String imageUrl = "http://example.com/image1.png";
        Label label = Label.createLabel(imageUrl);

        // when
        LabelResponseDTO dto = label.toLabelResponseDTO();

        // then
        assertNotNull(dto, "LabelResponseDTO는 null이 될 수 없음");
        assertEquals(imageUrl, dto.imageUrl(), "LabelResponseDTO의 label 값이 imageUrl과 일치해야 함");
    }
}

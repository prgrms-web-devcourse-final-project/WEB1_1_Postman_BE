package postman.bottler.label.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
    @DisplayName("label response dto 리스트를 label 도메인 리스트로 변환한다.")
    void toResponseDTOList() {
        // given
        Label label1 = Label.createLabel("http://example.com/image1.png");
        Label label2 = Label.createLabel("http://example.com/image2.png");
        List<Label> labels = List.of(label1, label2);

        // when
        List<LabelResponseDTO> dtos = Label.toResponseDTOList(labels);

        // then
        assertNotNull(dtos, "Response DTO 리스트는 null이 될 수 없음");
        assertEquals(2, dtos.size(), "DTO 리스트 사이즈는 2");
    }
}
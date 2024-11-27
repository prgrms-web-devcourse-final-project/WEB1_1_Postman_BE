package postman.bottler.label.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.exception.InvalidLabelException;

@SpringBootTest
class LabelTest {
    @Test
    @DisplayName("주어진 이미지 URL로 Label 객체가 생성된다.")
    void createLabelByImageURL() {
        // given
        String imageUrl = "http://example.com/image.png";

        // when
        Label label = Label.createLabel(imageUrl, 10);

        // then
        assertNotNull(label, "Label은 null이 될 수 없음");
        assertEquals(imageUrl, label.getImageUrl(), "주어진 url로 Label 생성");
    }

    @Test
    @DisplayName("Label 객체를 LabelResponseDTO로 변환한다.")
    void toLabelResponseDTO() {
        // given
        String imageUrl = "http://example.com/image1.png";
        Label label = Label.createLabel(imageUrl, 10);

        // when
        LabelResponseDTO dto = label.toLabelResponseDTO();

        // then
        assertNotNull(dto, "LabelResponseDTO는 null이 될 수 없음");
        assertEquals(imageUrl, dto.imageUrl(), "LabelResponseDTO의 label 값이 imageUrl과 일치해야 함");
    }

    @Test
    @DisplayName("주어진 labelId, imageUrl, limitCount, ownedCount 값으로 Label 객체가 생성된다.")
    void createLabelWithAllParameters() {
        // given
        Long labelId = 1L;
        String imageUrl = "http://example.com/image2.png";
        int limitCount = 5;
        int ownedCount = 2;

        // when
        Label label = Label.createLabel(labelId, imageUrl, limitCount, ownedCount);

        // then
        assertNotNull(label, "Label은 null이 될 수 없음");
        assertEquals(labelId, label.getLabelId(), "labelId가 일치해야 함");
        assertEquals(imageUrl, label.getImageUrl(), "imageUrl이 일치해야 함");
        assertEquals(limitCount, label.getLimitCount(), "limitCount가 일치해야 함");
        assertEquals(ownedCount, label.getOwnedCount(), "ownedCount가 일치해야 함");
    }

    @Test
    @DisplayName("ownedCount가 limitCount를 초과하지 않는 경우 isOwnedCountValid 메서드는 true를 반환한다.")
    void isOwnedCountValidReturnsTrue() {
        // given
        Label label = Label.createLabel("http://example.com/image3.png", 10);
        label.increaseOwnedCount();

        // when
        boolean result = label.isOwnedCountValid();

        // then
        assertTrue(result, "소유 인원수가 제한 인원수를 초과하지 않았을 때 true를 반환해야 함");
    }

    @Test
    @DisplayName("ownedCount가 limitCount를 초과한 경우 isOwnedCountValid 메서드는 false를 반환한다.")
    void isOwnedCountValidReturnsFalse() {
        // given
        Label label = Label.createLabel("http://example.com/image4.png", 1);
        label.increaseOwnedCount();
        label.increaseOwnedCount();

        // when
        boolean result = label.isOwnedCountValid();

        // then
        assertFalse(result, "소유 인원수가 제한 인원수에 도달했을 때 false를 반환해야 함");
    }

    @Test
    @DisplayName("ownedCount를 증가시킨 후 값이 올바르게 반영된다.")
    void increaseOwnedCount() {
        // given
        Label label = Label.createLabel("http://example.com/image5.png", 5);
        int initialOwnedCount = label.getOwnedCount();

        // when
        label.increaseOwnedCount();

        // then
        assertEquals(initialOwnedCount + 1, label.getOwnedCount(), "소유 인원수가 1 증가해야 함");
    }

    @Test
    @DisplayName("라벨 객체의 limitCount가 음수일 경우 예외를 발생시킨다.")
    void createLabelWithNegativeLimitCountThrowsException() {
        // given
        String imageUrl = "http://example.com/image6.png";
        int invalidLimitCount = -1;

        // when, then
        assertThrows(InvalidLabelException.class, () -> {
            Label.createLabel(imageUrl, invalidLimitCount);
        }, "limitCount가 음수일 경우 예외가 발생해야 함");
    }
}

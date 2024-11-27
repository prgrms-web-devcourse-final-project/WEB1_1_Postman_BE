package postman.bottler.label.domain;

import lombok.Getter;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.exception.FirstComeFirstServedLabelException;
import postman.bottler.label.exception.InvalidLabelException;

@Getter
public class Label {
    private Long labelId;
    private final String imageUrl;
    private final int limitCount; //최대 인원수
    private int ownedCount; //소유한 인원수

    private Label(Long labelId, String imageUrl, int limitCount, int ownedCount) {
        this.labelId = labelId;
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
        this.ownedCount = ownedCount;
    }

    private Label(String imageUrl, int limitCount) {
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
    }

    public static Label createLabel(Long labelId, String imageUrl, int limitCount, int ownedCount) {
        validateCount(limitCount);
        validateCount(ownedCount);
        isOwnedCountValid(limitCount, ownedCount);
        return new Label(labelId, imageUrl, limitCount, ownedCount);
    }

    public static Label createLabel(String imageUrl, int limitCount) {
        validateCount(limitCount);
        return new Label(imageUrl, limitCount);
    }

    private static void validateCount(int count) {
        if (count < 0) {
            throw new InvalidLabelException("라벨 인원수는 음수일 수 없습니다.");
        }
    }

    public void increaseOwnedCount() {
        this.ownedCount++;
    }

    public LabelResponseDTO toLabelResponseDTO() {
        return new LabelResponseDTO(this.labelId, this.imageUrl);
    }

    private static void isOwnedCountValid(int limitCount, int ownedCount) {
        if (limitCount <= ownedCount) {
            throw new FirstComeFirstServedLabelException("선착순 뽑기 마감됐습니다.");
        }
    }
}

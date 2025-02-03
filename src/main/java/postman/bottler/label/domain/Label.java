package postman.bottler.label.domain;

import lombok.Getter;
import postman.bottler.label.application.dto.LabelResponseDTO;
import postman.bottler.label.exception.InvalidLabelException;

@Getter
public class Label {
    private Long labelId;
    private final String imageUrl;
    private final int limitCount; //최대 인원수
    private int ownedCount; //소유한 인원수
    private LabelType labelType;

    private Label(Long labelId, String imageUrl, int limitCount, int ownedCount, LabelType labelType) {
        this.labelId = labelId;
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
        this.ownedCount = ownedCount;
        this.labelType = labelType;
    }

    private Label(String imageUrl, int limitCount) {
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
        this.labelType = LabelType.GENERAL;
    }

    public static Label createLabel(Long labelId, String imageUrl, int limitCount, int ownedCount, LabelType labelType) {
        validateCount(limitCount);
        validateCount(ownedCount);
        return new Label(labelId, imageUrl, limitCount, ownedCount, labelType);
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

    public boolean isOwnedCountValid() {
        return limitCount > ownedCount;
    }

    public void updateFirstComeLabel() {
        this.labelType = LabelType.FIRST_COME;
    }
}

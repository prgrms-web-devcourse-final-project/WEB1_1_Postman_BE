package postman.bottler.label.domain;

import postman.bottler.label.dto.response.LabelResponseDTO;

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
        return new Label(labelId, imageUrl, limitCount, ownedCount);
    }

    public static Label createLabel(String imageUrl, int limitCount) {
        return new Label(imageUrl, limitCount);
    }

    public Long getLabelId() {
        return labelId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public LabelResponseDTO toLabelResponseDTO() {
        return new LabelResponseDTO(this.labelId, this.imageUrl);
    }

    public boolean isOwnedCountValid() {
        return limitCount > ownedCount;
    }
}

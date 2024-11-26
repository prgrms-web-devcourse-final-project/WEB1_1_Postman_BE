package postman.bottler.label.domain;

import postman.bottler.label.dto.response.LabelResponseDTO;

public class Label {
    private Long labelId;
    private String imageUrl;
    private int limitCount;

    private Label(Long labelId, String imageUrl, int limitCount) {
        this.labelId = labelId;
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
    }

    private Label(String imageUrl, int limitCount) {
        this.imageUrl = imageUrl;
        this.limitCount = limitCount;
    }

    public static Label createLabel(Long labelId, String imageUrl, int limitCount) {
        return new Label(labelId, imageUrl, limitCount);
    }

    public static Label createLabel(String imageUrl, int limitCount) {
        return new Label(imageUrl, limitCount);
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
}

package postman.bottler.label.domain;

import postman.bottler.label.dto.response.LabelResponseDTO;

public class Label {
    private Long labelId;
    private String imageUrl;

    private Label(Long labelId, String imageUrl) {
        this.labelId = labelId;
        this.imageUrl = imageUrl;
    }

    private Label(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Label createLabel(Long labelId, String imageUrl) {
        return new Label(labelId, imageUrl);
    }

    public static Label createLabel(String imageUrl) {
        return new Label(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LabelResponseDTO toLabelResponseDTO() {
        return new LabelResponseDTO(this.labelId, this.imageUrl);
    }
}

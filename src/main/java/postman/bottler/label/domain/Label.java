package postman.bottler.label.domain;

import postman.bottler.label.dto.response.LabelResponseDTO;

public class Label {
    private String imageUrl;

    private Label(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public static Label createLabel(String imageUrl) {
        return new Label(imageUrl);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public LabelResponseDTO toLabelResponseDTO() {
        return new LabelResponseDTO(this.imageUrl);
    }
}

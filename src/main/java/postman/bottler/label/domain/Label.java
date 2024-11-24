package postman.bottler.label.domain;

import java.util.List;
import java.util.stream.Collectors;
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

    public static List<LabelResponseDTO> toResponseDTOList(List<Label> labels) {
        return labels.stream()
                .map(label -> new LabelResponseDTO(label.getImageUrl()))
                .collect(Collectors.toList());
    }
}

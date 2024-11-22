package postman.bottler.label.domain;

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
}

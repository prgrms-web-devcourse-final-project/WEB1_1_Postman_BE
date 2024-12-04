package postman.bottler.user.domain;

public enum Provider {
    LOCAL("일반 유저"),
    KAKAO("카카오 유저");

    private final String description;

    Provider(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

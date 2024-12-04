package postman.bottler.user.domain;

public enum EmailForm {
    EMAIL_AUTH("BOTTLER 이메일 인증 번호",
            "<html><body><h1>BOTTLER 인증 코드: %s</h1><p>인증 코드 유효시간은 5분입니다.</p></body></html>");

    private final String title;
    private final String contentTemplate;

    EmailForm(String title, String contentTemplate) {
        this.title = title;
        this.contentTemplate = contentTemplate;
    }

    public String getTitle() {
        return title;
    }

    public String getContent(String authCode) {
        return String.format(contentTemplate, authCode);
    }
}

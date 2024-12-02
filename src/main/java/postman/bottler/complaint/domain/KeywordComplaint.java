package postman.bottler.complaint.domain;

public class KeywordComplaint extends Complaint {
    private KeywordComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static KeywordComplaint create(Long letterId, Long reporterId, String description) {
        return new KeywordComplaint(letterId, reporterId, description);
    }

    private KeywordComplaint(Long id, Long letterId, Long reporterId, String description) {
        super(id, letterId, reporterId, description);
    }

    public static KeywordComplaint of(Long id, Long letterId, Long reporterId, String description) {
        return new KeywordComplaint(id, letterId, reporterId, description);
    }
}

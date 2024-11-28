package postman.bottler.complaint.domain;

public class KeywordComplaint extends Complaint {
    private KeywordComplaint(Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(letterId, reporterId, reportedUserId, description);
    }

    public static KeywordComplaint create(Long letterId, Long reporterId, Long reportedUserId, String description) {
        return new KeywordComplaint(letterId, reporterId, reportedUserId, description);
    }

    private KeywordComplaint(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(id, letterId, reporterId, reportedUserId, description);
    }

    public static KeywordComplaint of(Long id, Long letterId, Long reporterId, Long reportedUserId,
                                      String description) {
        return new KeywordComplaint(id, letterId, reporterId, reportedUserId, description);
    }
}

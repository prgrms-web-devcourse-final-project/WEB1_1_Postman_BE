package postman.bottler.complaint.domain;

public class KeywordReplyComplaint extends Complaint {
    private KeywordReplyComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static KeywordReplyComplaint create(Long letterId, Long reporterId, String description) {
        return new KeywordReplyComplaint(letterId, reporterId, description);
    }

    private KeywordReplyComplaint(Long id, Long letterId, Long reporterId, String description) {
        super(id, letterId, reporterId, description);
    }

    public static KeywordReplyComplaint of(Long id, Long letterId, Long reporterId, String description) {
        return new KeywordReplyComplaint(id, letterId, reporterId, description);
    }
}
package postman.bottler.complaint.domain;

public class ReplyComplaint extends Complaint {
    private ReplyComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static ReplyComplaint create(Long letterId, Long reporterId, String description) {
        return new ReplyComplaint(letterId, reporterId, description);
    }

    private ReplyComplaint(Long id, Long letterId, Long reporterId, String description) {
        super(id, letterId, reporterId, description);
    }

    public static ReplyComplaint of(Long id, Long letterId, Long reporterId, String description) {
        return new ReplyComplaint(id, letterId, reporterId, description);
    }
}

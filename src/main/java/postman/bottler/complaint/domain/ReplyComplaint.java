package postman.bottler.complaint.domain;

public class ReplyComplaint extends Complaint {
    private ReplyComplaint(Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(letterId, reporterId, reportedUserId, description);
    }

    public static ReplyComplaint create(Long letterId, Long reporterId, Long reportedUserId, String description) {
        return new ReplyComplaint(letterId, reporterId, reportedUserId, description);
    }

    private ReplyComplaint(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(id, letterId, reporterId, reportedUserId, description);
    }

    public static ReplyComplaint of(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        return new ReplyComplaint(id, letterId, reporterId, reportedUserId, description);
    }
}

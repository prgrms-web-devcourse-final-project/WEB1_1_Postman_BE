package postman.bottler.complaint.domain;

public class MapReplyComplaint extends Complaint {
    private MapReplyComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static MapReplyComplaint create(Long letterId, Long reporterId, String description) {
        return new MapReplyComplaint(letterId, reporterId, description);
    }

    private MapReplyComplaint(Long id, Long letterId, Long reporterId, String description) {
        super(id, letterId, reporterId, description);
    }

    public static MapReplyComplaint of(Long id, Long letterId, Long reporterId, String description) {
        return new MapReplyComplaint(id, letterId, reporterId, description);
    }
}

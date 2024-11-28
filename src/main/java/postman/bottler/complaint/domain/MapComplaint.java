package postman.bottler.complaint.domain;

public class MapComplaint extends Complaint {
    private MapComplaint(Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(letterId, reporterId, reportedUserId, description);
    }

    public static MapComplaint create(Long letterId, Long reporterId, Long reportedUserId, String description) {
        return new MapComplaint(letterId, reporterId, reportedUserId, description);
    }

    private MapComplaint(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        super(id, letterId, reporterId, reportedUserId, description);
    }

    public static MapComplaint of(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        return new MapComplaint(id, letterId, reporterId, reportedUserId, description);
    }
}

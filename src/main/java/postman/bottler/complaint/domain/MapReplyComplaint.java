package postman.bottler.complaint.domain;

import java.time.LocalDateTime;

public class MapReplyComplaint extends Complaint {
    private MapReplyComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static MapReplyComplaint create(Long letterId, Long reporterId, String description) {
        return new MapReplyComplaint(letterId, reporterId, description);
    }

    private MapReplyComplaint(Long id, Long letterId, Long reporterId, String description, LocalDateTime createdAt) {
        super(id, letterId, reporterId, description, createdAt);
    }

    public static MapReplyComplaint of(Long id, Long letterId, Long reporterId, String description,
                                       LocalDateTime createdAt) {
        return new MapReplyComplaint(id, letterId, reporterId, description, createdAt);
    }
}

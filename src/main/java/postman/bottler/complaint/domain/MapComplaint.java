package postman.bottler.complaint.domain;

import java.time.LocalDateTime;

public class MapComplaint extends Complaint {
    private MapComplaint(Long letterId, Long reporterId, String description) {
        super(letterId, reporterId, description);
    }

    public static MapComplaint create(Long letterId, Long reporterId, String description) {
        return new MapComplaint(letterId, reporterId, description);
    }

    private MapComplaint(Long id, Long letterId, Long reporterId, String description, LocalDateTime createdAt) {
        super(id, letterId, reporterId, description, createdAt);
    }

    public static MapComplaint of(Long id, Long letterId, Long reporterId, String description,
                                  LocalDateTime createdAt) {
        return new MapComplaint(id, letterId, reporterId, description, createdAt);
    }
}

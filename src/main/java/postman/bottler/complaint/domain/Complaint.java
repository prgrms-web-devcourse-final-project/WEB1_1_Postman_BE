package postman.bottler.complaint.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import postman.bottler.complaint.exception.DuplicateComplainException;

@Getter
public class Complaint {
    private Long id;

    private final Long letterId;

    private final Long reporterId;

    private final String description;

    private final LocalDateTime createdAt;

    protected Complaint(Long letterId, Long reporterId, String description) {
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.description = description;
        this.createdAt = LocalDateTime.now();
    }

    protected Complaint(Long id, Long letterId, Long reporterId, String description, LocalDateTime createdAt) {
        this.id = id;
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.description = description;
        this.createdAt = createdAt;
    }

    public static Complaint create(Long letterId, Long reporterId, String description) {
        return new Complaint(letterId, reporterId, description);
    }

    public static Complaint of(Long id, Long letterId, Long reporterId, String description, LocalDateTime createdAt) {
        return new Complaint(id, letterId, reporterId, description, createdAt);
    }


    public Boolean isReporter(Long reporterId) {
        return this.reporterId.equals(reporterId);
    }

    public void validateDuplicateComplaint(Long letterId, Long reporterId) {
        if (letterId.equals(this.letterId) && reporterId.equals(this.reporterId)) {
            throw new DuplicateComplainException();
        }
    }
}

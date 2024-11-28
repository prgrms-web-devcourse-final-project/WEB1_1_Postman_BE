package postman.bottler.complaint.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Complaint {
    private Long id;

    private Long letterId;

    private Long reporterId;

    private Long reportedUserId;

    private String description;

    protected Complaint(Long letterId, Long reporterId, Long reportedUserId, String description) {
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.description = description;
    }

    protected Complaint(Long id, Long letterId, Long reporterId, Long reportedUserId, String description) {
        this.id = id;
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.reportedUserId = reportedUserId;
        this.description = description;
    }

    public Boolean isReporter(Long reporterId) {
        return this.reporterId.equals(reporterId);
    }
}

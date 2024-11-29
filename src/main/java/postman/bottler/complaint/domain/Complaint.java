package postman.bottler.complaint.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Complaint {
    private Long id;

    private Long letterId;

    private Long reporterId;

    private String description;

    protected Complaint(Long letterId, Long reporterId, String description) {
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.description = description;
    }

    protected Complaint(Long id, Long letterId, Long reporterId, String description) {
        this.id = id;
        this.letterId = letterId;
        this.reporterId = reporterId;
        this.description = description;
    }

    public Boolean isReporter(Long reporterId) {
        return this.reporterId.equals(reporterId);
    }
}

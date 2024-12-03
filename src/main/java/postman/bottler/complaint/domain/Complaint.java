package postman.bottler.complaint.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Complaint {
    private Long id;

    private Long letterId;

    private Long reporterId;

    private String description;

    private LocalDateTime createdAt;

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

    public Boolean isReporter(Long reporterId) {
        return this.reporterId.equals(reporterId);
    }
}

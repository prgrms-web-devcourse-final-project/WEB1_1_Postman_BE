package postman.bottler.complaint.infra.keyword;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import postman.bottler.complaint.domain.KeywordComplaint;

@Entity
@Table(name = "keyword_complaint")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long letterId;

    private Long reporterId;

    private Long reportedUserId;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static KeywordComplaintEntity from(KeywordComplaint complaint) {
        return KeywordComplaintEntity.builder()
                .id(complaint.getId())
                .letterId(complaint.getLetterId())
                .reporterId(complaint.getReporterId())
                .reportedUserId(complaint.getReportedUserId())
                .description(complaint.getDescription())
                .build();
    }

    public KeywordComplaint toDomain() {
        return KeywordComplaint.of(id, letterId, reporterId, reportedUserId, description);
    }
}

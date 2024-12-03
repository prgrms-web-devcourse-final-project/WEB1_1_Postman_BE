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
import postman.bottler.complaint.domain.KeywordReplyComplaint;

@Entity
@Table(name = "keyword_reply_complaint")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class KeywordReplyComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long letterId;

    private Long reporterId;

    private String description;

    private LocalDateTime createdAt;

    public static KeywordReplyComplaintEntity from(KeywordReplyComplaint complaint) {
        return KeywordReplyComplaintEntity.builder()
                .id(complaint.getId())
                .letterId(complaint.getLetterId())
                .reporterId(complaint.getReporterId())
                .description(complaint.getDescription())
                .createdAt(complaint.getCreatedAt())
                .build();
    }

    public KeywordReplyComplaint toDomain() {
        return KeywordReplyComplaint.of(id, letterId, reporterId, description, createdAt);
    }
}

package postman.bottler.complaint.infra.map;

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
import postman.bottler.complaint.domain.MapReplyComplaint;

@Entity
@Table(name = "map_reply_complaint")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MapReplyComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long letterId;

    private Long reporterId;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static MapReplyComplaintEntity from(MapReplyComplaint complaint) {
        return MapReplyComplaintEntity.builder()
                .id(complaint.getId())
                .letterId(complaint.getLetterId())
                .reporterId(complaint.getReporterId())
                .description(complaint.getDescription())
                .build();
    }

    public MapReplyComplaint toDomain() {
        return MapReplyComplaint.of(id, letterId, reporterId, description);
    }
}

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
import postman.bottler.complaint.domain.MapComplaint;

@Entity
@Table(name = "map_complaint")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MapComplaintEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long letterId;

    private Long reporterId;

    private Long reportedUserId;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public static MapComplaintEntity from(MapComplaint complaint) {
        return MapComplaintEntity.builder()
                .id(complaint.getId())
                .letterId(complaint.getLetterId())
                .reporterId(complaint.getReporterId())
                .reportedUserId(complaint.getReportedUserId())
                .description(complaint.getDescription())
                .build();
    }

    public MapComplaint toDomain() {
        return MapComplaint.of(id, letterId, reporterId, reportedUserId, description);
    }
}
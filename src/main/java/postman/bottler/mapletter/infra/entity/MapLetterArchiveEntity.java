package postman.bottler.mapletter.infra.entity;

import com.google.firebase.database.annotations.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.mapletter.domain.MapLetterArchive;

@Entity
@Table(name = "map_letter_archive")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MapLetterArchiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapLetterArchiveId;
    @NotNull
    private Long mapLetterId;
    @NotNull
    private Long userId;
    private LocalDateTime createdAt;

    @Builder
    public MapLetterArchiveEntity(Long mapLetterArchiveId, Long mapLetterId, Long userId, LocalDateTime createdAt) {
        this.mapLetterArchiveId = mapLetterArchiveId;
        this.mapLetterId = mapLetterId;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public static MapLetterArchiveEntity from(MapLetterArchive mapLetterArchive) {
        return MapLetterArchiveEntity.builder()
                .mapLetterArchiveId(mapLetterArchive.getMapLetterArchiveId())
                .mapLetterId(mapLetterArchive.getMapLetterId())
                .userId(mapLetterArchive.getUserId())
                .createdAt(mapLetterArchive.getCreatedAt())
                .build();
    }

    public static MapLetterArchive toDomain(MapLetterArchiveEntity mapLetterArchiveEntity) {
        return MapLetterArchive.builder()
                .mapLetterArchiveId(mapLetterArchiveEntity.getMapLetterArchiveId())
                .mapLetterId(mapLetterArchiveEntity.getMapLetterId())
                .userId(mapLetterArchiveEntity.getUserId())
                .createdAt(mapLetterArchiveEntity.getCreatedAt())
                .build();
    }
}

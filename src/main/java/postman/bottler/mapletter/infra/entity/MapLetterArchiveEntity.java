package postman.bottler.mapletter.infra.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.mapletter.domain.MapLetterArchive;

@Entity
@Table(name = "map_letter_archive_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MapLetterArchiveEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapLetterArchiveId;
    private Long mapLetterId;
    private Long userId;

    @Builder
    public MapLetterArchiveEntity(Long mapLetterArchiveId, Long mapLetterId, Long userId) {
        this.mapLetterArchiveId = mapLetterArchiveId;
        this.mapLetterId = mapLetterId;
        this.userId = userId;
    }

    public static MapLetterArchiveEntity from(MapLetterArchive mapLetterArchive) {
        return MapLetterArchiveEntity.builder()
                .mapLetterArchiveId(mapLetterArchive.getMapLetterArchiveId())
                .mapLetterId(mapLetterArchive.getMapLetterId())
                .userId(mapLetterArchive.getUserId())
                .build();
    }

    public static MapLetterArchive toDomain(MapLetterArchiveEntity mapLetterArchiveEntity) {
        return MapLetterArchive.builder()
                .mapLetterArchiveId(mapLetterArchiveEntity.getMapLetterArchiveId())
                .mapLetterId(mapLetterArchiveEntity.getMapLetterId())
                .userId(mapLetterArchiveEntity.getUserId())
                .build();
    }
}

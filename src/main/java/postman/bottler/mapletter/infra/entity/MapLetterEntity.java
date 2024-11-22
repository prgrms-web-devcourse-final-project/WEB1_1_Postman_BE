package postman.bottler.mapletter.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.mapletter.domain.MapLetter;
import postman.bottler.mapletter.domain.MapLetterType;

@Entity
@Table(name = "map_letter_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class MapLetterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapLetterId;
    private String title;
    private String content;
    @Column(precision = 10, scale = 7) //총 10자리, 소수점 7자리(3, 7)
    private BigDecimal latitude;
    @Column(precision = 11, scale = 7) //총 11자리, 소수점 7자리(4, 7)
    private BigDecimal longitude;
    private String font;
    private int paper;
    private int label;

    @Enumerated(EnumType.STRING)
    private MapLetterType type;
    private Long targetUserId;
    private Long createUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBlocked;

    @Builder
    public MapLetterEntity(String title, String content, BigDecimal latitude, BigDecimal longitude, String font,
                           int paper, int label, MapLetterType type, Long targetUserId, Long createUserId,
                           LocalDateTime createdAt, LocalDateTime updatedAt, boolean isDeleted, boolean isBlocked) {
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
        this.font = font;
        this.paper = paper;
        this.label = label;
        this.type = type;
        this.targetUserId = targetUserId;
        this.createUserId = createUserId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDeleted = isDeleted;
        this.isBlocked = isBlocked;
    }

    public static MapLetterEntity from(MapLetter mapLetter) {
        return MapLetterEntity.builder()
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .latitude(mapLetter.getLatitude())
                .longitude(mapLetter.getLongitude())
                .font(mapLetter.getFont())
                .paper(mapLetter.getPaper())
                .label(mapLetter.getLabel())
                .type(mapLetter.getType())
                .targetUserId(mapLetter.getTargetUserId())
                .createUserId(mapLetter.getCreateUserId())
                .createdAt(mapLetter.getCreatedAt())
                .updatedAt(mapLetter.getUpdatedAt())
                .isDeleted(mapLetter.isDeleted())
                .isBlocked(mapLetter.isBlocked())
                .build();
    }

    public static MapLetter toDomain(MapLetterEntity mapLetterEntity) {
        return MapLetter.builder()
                .id(mapLetterEntity.mapLetterId)
                .title(mapLetterEntity.title)
                .content(mapLetterEntity.content)
                .latitude(mapLetterEntity.latitude)
                .longitude(mapLetterEntity.longitude)
                .font(mapLetterEntity.font)
                .paper(mapLetterEntity.paper)
                .label(mapLetterEntity.label)
                .type(mapLetterEntity.type)
                .targetUserId(mapLetterEntity.targetUserId)
                .createUserId(mapLetterEntity.createUserId)
                .createdAt(mapLetterEntity.createdAt)
                .updatedAt(mapLetterEntity.updatedAt)
                .isDeleted(mapLetterEntity.isDeleted)
                .isBlocked(mapLetterEntity.isBlocked)
                .build();
    }
}

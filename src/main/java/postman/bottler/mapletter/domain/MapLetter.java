package postman.bottler.mapletter.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindMapLetter;
import postman.bottler.mapletter.dto.response.OneLetterResponse;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MapLetter {
    private Long id;
    private String title;
    private String content;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private String font;
    private String paper;
    private String label;
    private String description;
    private MapLetterType type;
    private Long targetUserId;
    private Long createUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isDeleted;
    private boolean isBlocked;

    public static MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId) {
        return MapLetter.builder()
                .title(createPublicMapLetterRequestDTO.title())
                .content(createPublicMapLetterRequestDTO.content())
                .latitude(createPublicMapLetterRequestDTO.latitude())
                .longitude(createPublicMapLetterRequestDTO.longitude())
                .font(createPublicMapLetterRequestDTO.font())
                .paper(createPublicMapLetterRequestDTO.paper())
                .label(createPublicMapLetterRequestDTO.label())
                .type(MapLetterType.PUBLIC)
                .createUserId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .isBlocked(false)
                .description(createPublicMapLetterRequestDTO.description())
                .build();
    }

    public static MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO, Long userId) {
        return MapLetter.builder()
                .title(createTargetMapLetterRequestDTO.title())
                .content(createTargetMapLetterRequestDTO.content())
                .latitude(createTargetMapLetterRequestDTO.latitude())
                .longitude(createTargetMapLetterRequestDTO.longitude())
                .font(createTargetMapLetterRequestDTO.font())
                .paper(createTargetMapLetterRequestDTO.paper())
                .label(createTargetMapLetterRequestDTO.label())
                .type(MapLetterType.PRIVATE)
                .createUserId(userId)
                .targetUserId(createTargetMapLetterRequestDTO.target())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .isBlocked(false)
                .description(createTargetMapLetterRequestDTO.description())
                .build();
    }

    public static OneLetterResponse toOneLetterResponse(MapLetter mapLetter) {
        return OneLetterResponse.builder()
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .font(mapLetter.getFont())
                .paper(mapLetter.getPaper())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .description(mapLetter.getDescription())
                .build();
    }

    public static FindMapLetter toFindSentMapLetter(MapLetter mapLetter) {
        return FindMapLetter.builder()
                .letterId(mapLetter.getId())
                .title(mapLetter.getTitle())
                .label(mapLetter.getLabel())
                .createdAt(mapLetter.getCreatedAt())
                .build();
    }

    public void updateDelete(boolean deleted) {
        this.isDeleted = deleted;
    }
}

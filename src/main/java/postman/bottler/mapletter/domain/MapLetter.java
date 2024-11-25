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
    private int paper;
    private int label;
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
                .build();
    }

    public static OneLetterResponse toOneLetterResponse(MapLetter mapLetter) {
        return OneLetterResponse.builder()
                .title(mapLetter.getTitle())
                .content(mapLetter.getContent())
                .font(mapLetter.getFont())
                .createdAt(mapLetter.getCreatedAt())
                .build();
    }
}

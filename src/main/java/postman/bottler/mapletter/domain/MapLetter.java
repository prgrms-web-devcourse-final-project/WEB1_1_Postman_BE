package postman.bottler.mapletter.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.application.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.exception.BlockedLetterException;
import postman.bottler.mapletter.exception.DistanceToFarException;
import postman.bottler.mapletter.exception.MapLetterAlreadyDeletedException;

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
    private boolean isRead;
    private boolean isRecipientDeleted;

    public static MapLetter createPublicMapLetter(CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
                                                  Long userId) {
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
                .isRead(false)
                .description(createPublicMapLetterRequestDTO.description())
                .isRecipientDeleted(false)
                .build();
    }

    public static MapLetter createTargetMapLetter(CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO,
                                                  Long userId, Long targetUserId) {
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
                .targetUserId(targetUserId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDeleted(false)
                .isBlocked(false)
                .isRead(false)
                .description(createTargetMapLetterRequestDTO.description())
                .isRecipientDeleted(false)
                .build();
    }

    public void updateDelete(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updateRecipientDeleted(boolean deleted) {
        this.isRecipientDeleted = deleted;
    }

    public void validateFindOneMapLetter(double viewDistance, Double distance) {
        validDeleteAndBlocked();
        if (distance > viewDistance) {
            throw new DistanceToFarException("편지와의 거리가 멀어서 조회가 불가능합니다.");
        }
    }

    public void validateAccess(Long userId) {
        if (this.getType() == MapLetterType.PRIVATE && (!this.getTargetUserId().equals(userId)
                && !this.getCreateUserId().equals(userId))) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        validDeleteAndBlocked();
    }

    public void validDeleteMapLetter(Long userId) {
        if (!this.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
        }
        if (this.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }
    }

    public void validFindAllReplyMapLetter(Long userId) {
        if (!this.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        validDeleteAndBlocked();
    }

    public void validMapLetterArchive() {
        if (this.getType() == MapLetterType.PRIVATE) {
            throw new CommonForbiddenException("편지를 저장할 수 있는 권한이 없습니다.");
        }
        validDeleteAndBlocked();
    }

    public void validDeleteAndBlocked() {
        if (this.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }
        if (this.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }
    }

    public void isPrivate() {
        if (this.getType() == MapLetterType.PRIVATE) {
            throw new CommonForbiddenException("해당 편지에 접근할 수 없습니다.");
        }
    }

    public boolean isTargetUser(Long userId) {
        return this.targetUserId != null && this.targetUserId.equals(userId);
    }

    public void validateRecipientDeletion(Long userId) {
        if (this.getTargetUserId() == null || !this.getTargetUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
        }
        if (this.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }
        if (this.isRecipientDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 이미 삭제되었습니다.");
        }
    }
}

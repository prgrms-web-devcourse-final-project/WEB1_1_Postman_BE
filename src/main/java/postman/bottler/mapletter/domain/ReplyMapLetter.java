package postman.bottler.mapletter.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.global.exception.CommonForbiddenException;
import postman.bottler.mapletter.application.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.exception.BlockedLetterException;
import postman.bottler.mapletter.exception.MapLetterAlreadyDeletedException;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReplyMapLetter {
    private Long replyLetterId;
    private Long sourceLetterId;
    private String font;
    private String paper;
    private String label;
    private String content;
    private boolean isBlocked;
    private boolean isDeleted;
    private Long createUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private boolean isRecipientDeleted;

    public static ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
                                                      Long userId) {
        return ReplyMapLetter.builder()
                .sourceLetterId(createReplyMapLetterRequestDTO.sourceLetter())
                .font(createReplyMapLetterRequestDTO.font())
                .content(createReplyMapLetterRequestDTO.content())
                .paper(createReplyMapLetterRequestDTO.paper())
                .label(createReplyMapLetterRequestDTO.label())
                .isBlocked(false)
                .isDeleted(false)
                .createUserId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isRecipientDeleted(false)
                .build();
    }

    public void updateDelete(boolean deleted) {
        this.isDeleted = deleted;
    }

    public void updateRecipientDeleted(boolean deleted) {
        this.isRecipientDeleted = deleted;
    }

    public void validFindOneReplyMapLetter(Long userId, MapLetter sourceLetter) {
        if (!this.getCreateUserId().equals(userId) && !sourceLetter.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 볼 수 있는 권한이 없습니다.");
        }
        validDeleteAndBlocked();
    }

    public void validDeleteReplyMapLetter(Long userId) {
        if (!this.getCreateUserId().equals(userId)) {
            throw new CommonForbiddenException("편지를 삭제 할 권한이 없습니다. 편지 삭제에 실패하였습니다.");
        }
        validDeleteAndBlocked();
    }

    private void validDeleteAndBlocked() {
        if (this.isDeleted()) {
            throw new MapLetterAlreadyDeletedException("해당 편지는 삭제되었습니다.");
        }
        if (this.isBlocked()) {
            throw new BlockedLetterException("해당 편지는 신고당한 편지입니다.");
        }
    }

    public void validateRecipientDeletion(Long userId, Long sourceLetterCreateUserId) {
        if (!sourceLetterCreateUserId.equals(userId)) {
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

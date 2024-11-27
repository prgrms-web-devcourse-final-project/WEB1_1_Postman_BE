package postman.bottler.mapletter.domain;

import lombok.*;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ReplyMapLetter {
    private Long mapLetterId;
    private Long sourceLetterId;
    private String font;
    private String paper;
    private String label;
    private boolean isBlocked;
    private boolean isDeleted;
    private Long createUserId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO, Long userId) {
        return ReplyMapLetter.builder()
                .sourceLetterId(createReplyMapLetterRequestDTO.sourceLetter())
                .font(createReplyMapLetterRequestDTO.font())
                .paper(createReplyMapLetterRequestDTO.paper())
                .label(createReplyMapLetterRequestDTO.label())
                .isBlocked(false)
                .isDeleted(false)
                .createUserId(userId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public void updateDelete(boolean deleted) {
        this.isDeleted = deleted;
    }
}


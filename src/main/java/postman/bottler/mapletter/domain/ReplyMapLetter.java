package postman.bottler.mapletter.domain;

import lombok.*;
import postman.bottler.mapletter.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.FindAllReplyMapLettersResponseDTO;

import java.time.LocalDateTime;

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

    public static ReplyMapLetter createReplyMapLetter(CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO, Long userId) {
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
                .build();
    }

    public void updateDelete(boolean deleted) {
        this.isDeleted = deleted;
    }

    public static FindAllReplyMapLettersResponseDTO toFindAllReplyMapLettersResponseDTO(ReplyMapLetter replyMapLetter) {
        return FindAllReplyMapLettersResponseDTO.builder()
                .replyLetterId(replyMapLetter.getReplyLetterId())
                .label(replyMapLetter.getLabel())
                .createdAt(replyMapLetter.getCreatedAt())
                .build();
    }

}


package postman.bottler.mapletter.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.global.exception.CommonForbiddenException;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MapLetterArchive {
    private Long mapLetterArchiveId;
    private Long mapLetterId;
    private Long userId;
    private LocalDateTime createdAt;

    public void validDeleteArchivedLetter(Long userId) {
        if (!this.getUserId().equals(userId)) {
            throw new CommonForbiddenException("편지 보관 취소를 할 권한이 없습니다. 편지 보관 취소에 실패했습니다.");
        }
    }
}

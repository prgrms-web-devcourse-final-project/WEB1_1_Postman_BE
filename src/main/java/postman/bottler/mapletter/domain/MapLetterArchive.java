package postman.bottler.mapletter.domain;

import java.time.LocalDateTime;
import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class MapLetterArchive {
    private Long mapLetterArchiveId;
    private Long mapLetterId;
    private Long userId;
    private LocalDateTime createdAt;
}

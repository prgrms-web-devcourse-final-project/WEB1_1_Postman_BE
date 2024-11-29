package postman.bottler.mapletter.domain;

import lombok.*;
import postman.bottler.mapletter.dto.PaperDTO;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Paper {
    Long paperId;
    String paperUrl;

    public static PaperDTO toPaperDTO(Paper paper) {
        return new PaperDTO(paper.paperId, paper.paperUrl);
    }
}

package postman.bottler.letter.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SavedLetter {
    private Long id;
    private Long userId;
    private Long letterId;
    private boolean isDeleted;

    @Builder
    private SavedLetter(Long id, Long userId, Long letterId, boolean isDeleted) {
        this.id = id;
        this.userId = userId;
        this.letterId = letterId;
        this.isDeleted = isDeleted;
    }
}

package postman.bottler.letter.domain;

import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class LetterBox {
    private Long id;
    private Long userId;
    private Long letterId;
    private LetterType letterType;
    private BoxType boxType;
    private LocalDateTime createdAt;
}

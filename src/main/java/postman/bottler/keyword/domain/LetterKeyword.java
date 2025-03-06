package postman.bottler.keyword.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LetterKeyword {

    private Long id;
    private Long letterId;
    private String keyword;
    private boolean isDeleted;

    public static LetterKeyword from(Long letterId, String keyword) {
        return LetterKeyword.builder().letterId(letterId).keyword(keyword).isDeleted(false).build();
    }
}

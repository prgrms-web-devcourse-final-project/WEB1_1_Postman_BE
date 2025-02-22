package postman.bottler.mapletter.application.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.List;
import postman.bottler.mapletter.exception.TypeNotFoundException;

public record DeleteMapLettersRequestDTO(
        List<LetterInfo> letters
) {
    public enum LetterType {
        REPLY, MAP;

        @JsonCreator
        public static LetterType from(String value) {
            for (LetterType type : LetterType.values()) {
                if (type.name().equals(value)) {
                    return type;
                }
            }
            throw new TypeNotFoundException("잘못된 편지 타입입니다.");
        }
    }

    public record LetterInfo(
            LetterType letterType,
            Long letterId
    ) {
    }
}

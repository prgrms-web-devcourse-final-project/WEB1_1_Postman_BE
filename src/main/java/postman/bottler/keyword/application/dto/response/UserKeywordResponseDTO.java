package postman.bottler.keyword.application.dto.response;

import java.util.List;
import postman.bottler.keyword.domain.UserKeyword;

public record UserKeywordResponseDTO(
        List<String> keywords
) {
    public static UserKeywordResponseDTO from(List<UserKeyword> userKeywords) {
        // UserKeyword의 keyword 값을 추출하여 리스트로 변환
        List<String> keywords = userKeywords.stream()
                .map(UserKeyword::getKeyword)
                .toList();

        return new UserKeywordResponseDTO(keywords);
    }
}

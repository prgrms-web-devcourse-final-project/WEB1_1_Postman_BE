package postman.bottler.keyword.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import postman.bottler.keyword.domain.Keyword;

public record KeywordResponseDTO(
        List<CategoryKeywordsDTO> categories
) {
    // 정적 팩토리 메서드로 변환 로직 처리
    public static KeywordResponseDTO from(List<Keyword> keywordList) {
        // 카테고리별로 키워드 그룹화
        Map<String, List<String>> groupedByCategory = keywordList.stream()
                .collect(Collectors.groupingBy(
                        Keyword::getCategory,
                        Collectors.mapping(Keyword::getKeyword, Collectors.toList())
                ));

        // 카테고리 DTO 리스트 생성
        List<CategoryKeywordsDTO> categories = groupedByCategory.entrySet().stream()
                .map(entry -> new CategoryKeywordsDTO(entry.getKey(), entry.getValue()))
                .toList();

        return new KeywordResponseDTO(categories);
    }

    public record CategoryKeywordsDTO(
            String category,
            List<String> keywords
    ) {
    }
}

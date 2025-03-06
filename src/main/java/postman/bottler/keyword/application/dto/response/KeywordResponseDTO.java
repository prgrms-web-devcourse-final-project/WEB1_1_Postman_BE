package postman.bottler.keyword.application.dto.response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import postman.bottler.keyword.domain.Keyword;

public record KeywordResponseDTO(List<CategoryKeywordsDTO> categories) {
    public static KeywordResponseDTO from(List<Keyword> keywordList) {
        Map<String, List<String>> groupedByCategory = keywordList.stream().collect(
                Collectors.groupingBy(Keyword::getCategory,
                        Collectors.mapping(Keyword::getKeyword, Collectors.toList())));

        List<CategoryKeywordsDTO> categories = groupedByCategory.entrySet().stream()
                .map(entry -> new CategoryKeywordsDTO(entry.getKey(), entry.getValue())).toList();

        return new KeywordResponseDTO(categories);
    }

    public record CategoryKeywordsDTO(String category, List<String> keywords) {
    }
}

package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.repository.KeywordRepository;
import postman.bottler.keyword.domain.Keyword;
import postman.bottler.keyword.application.dto.response.KeywordResponseDTO;

@Service
@RequiredArgsConstructor
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public KeywordResponseDTO getKeywords() {
        List<Keyword> keywords = keywordRepository.getKeywords();
        return KeywordResponseDTO.from(keywords);
    }
}

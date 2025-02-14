package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.dto.response.KeywordResponseDTO;
import postman.bottler.keyword.application.repository.KeywordRepository;
import postman.bottler.keyword.domain.Keyword;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordService {

    private final KeywordRepository keywordRepository;

    @Transactional(readOnly = true)
    public KeywordResponseDTO getKeywords() {
        log.debug("키워드 조회 요청");

        List<Keyword> keywords = keywordRepository.getKeywords();

        log.info("키워드 조회 완료: 조회된 키워드 개수={}", keywords.size());

        return KeywordResponseDTO.from(keywords);
    }
}

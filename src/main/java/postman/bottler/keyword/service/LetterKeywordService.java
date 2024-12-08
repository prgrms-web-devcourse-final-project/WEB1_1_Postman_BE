package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.dto.response.FrequentKeywordsDTO;
import postman.bottler.letter.service.LetterService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterKeywordService {

    private final LetterKeywordRepository letterKeywordRepository;
    private final LetterService letterService;

    @Transactional
    public void createLetterKeywords(Long letterId, List<String> keywords) {
        List<LetterKeyword> letterKeywords = keywords.stream()
                .map(keyword -> LetterKeyword.from(letterId, keyword))
                .toList();

        letterKeywordRepository.saveAll(letterKeywords);
    }

    @Transactional(readOnly = true)
    public List<String> getKeywords(Long letterId) {
        List<LetterKeyword> letterKeywords = letterKeywordRepository.getKeywordsByLetterId(letterId);

        return letterKeywords.stream()
                .map(LetterKeyword::getKeyword).toList();
    }

    @Transactional
    public void markKeywordsAsDeleted(List<Long> letterIds) {
        letterKeywordRepository.markKeywordsAsDeleted(letterIds);
    }


    @Transactional(readOnly = true)
    public FrequentKeywordsDTO getTopFrequentKeywords(Long userId) {
        List<Long> letterIds = letterService.findAllByUserId(userId);
        List<LetterKeyword> frequentKeywords = letterKeywordRepository.getFrequentKeywords(letterIds);
        List<String> keywords = frequentKeywords.stream()
                .map(LetterKeyword::getKeyword)
                .toList();
        return FrequentKeywordsDTO.from(keywords);
    }
}

package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.keyword.service.LetterKeywordService;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;

@Service
@RequiredArgsConstructor
public class LetterFacadeService {

    private final LetterService letterService;
    private final LetterKeywordService letterKeywordService;

    @Transactional
    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        Letter letter = letterService.createLetter(letterRequestDTO, userId);
        List<LetterKeyword> keywords = letterKeywordService.createLetterKeywords(
                letter.getId(),
                letterRequestDTO.keywords()
        );
        return LetterResponseDTO.from(letter, keywords);
    }

}

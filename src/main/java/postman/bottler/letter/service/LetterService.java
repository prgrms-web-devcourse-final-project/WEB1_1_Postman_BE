package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterResponseDTO createLetter(LetterRequestDTO request) {
        return null;
    }

    public Page<LetterKeywordsResponseDTO> getSentLetters(int page, int size, String sort) {
        return null;
    }

    public Letter getLetterDetail(Long letterId) {
        return null;
    }

    public void saveLetter(Long letterId) {

    }

    public List<Letter> getSavedLetters(Pageable pageable) {
        return List.of();
    }

    public void deleteSavedLetter(Long letterId) {

    }

    public void incrementWarningCount(Long letterId) {

    }
}

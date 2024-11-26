package postman.bottler.letter.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;

@Service
public class SavedLetterService {

    public void saveLetter(Long letterId) {

    }

    public Page<LetterKeywordsResponseDTO> getSavedLetters(int page, int size) {
        return null;
    }

    public void deleteSavedLetter(Long letterId) {

    }
}

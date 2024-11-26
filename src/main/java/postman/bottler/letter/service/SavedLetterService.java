package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.SavedLetter;
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;
import postman.bottler.letter.exception.LetterAlreadySavedException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavedLetterService {

    private final SavedLetterRepository savedLetterRepository;
    private final LetterRepository letterRepository;

    public void saveLetter(Long letterId) {
        Long userId = 1L;

        validateCanBeSaved(userId, letterId);
        validateLetterExists(letterId);

        SavedLetter savedLetter = SavedLetter.builder()
                .userId(userId)
                .letterId(letterId)
                .build();

        savedLetterRepository.save(savedLetter);
    }

    public Page<LetterKeywordsResponseDTO> getSavedLetters(int page, int size) {
        return null;
    }

    public void deleteSavedLetter(Long letterId) {

    }

    private void validateLetterExists(Long letterId) {
        if (!letterRepository.existsById(letterId)) {
            throw new LetterNotFoundException("키워드 편지가 존재하지 않습니다.");
        }
    }

    private void validateCanBeSaved(Long userId, Long letterId) {
        if (savedLetterRepository.isAlreadySaved(userId, letterId)) {
            throw new LetterAlreadySavedException("이미 저장된 편지입니다.");
        }
    }
}

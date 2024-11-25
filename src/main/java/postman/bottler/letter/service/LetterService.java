package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO) {
        Letter letter = letterRepository.save(letterRequestDTO.toDomain(1L));
        return LetterResponseDTO.from(letter);
    }

    public Page<LetterKeywordsResponseDTO> getLetterKeywords(int page, int size, String sort) {
        return null;
    }

    public LetterResponseDTO getLetterDetail(Long letterId) {
        return null;
    }

    public void saveLetter(Long letterId) {

    }

    public Page<LetterKeywordsResponseDTO> getSavedLetters(int page, int size) {
        return null;
    }

    public void deleteSavedLetter(Long letterId) {

    }

    public void incrementWarningCount(Long letterId) {

    }

    public void deleteLetter(Long letterId) {

    }
}

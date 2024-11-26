package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterKeywordsResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.LetterNotFoundException;

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

    public void deleteLetter(Long letterId) {
        validateLetterExists(letterId);
        letterRepository.remove(letterId);
    }

    public LetterResponseDTO getLetterDetail(Long letterId) {
        Letter letter = findLetter(letterId);
        return LetterResponseDTO.from(letter);
    }

    public void incrementWarningCount(Long letterId) {

    }

    private void validateLetterExists(Long letterId) {
        if (letterRepository.existsById(letterId)) {
            throw new LetterNotFoundException("키워드 편지가 존재하지 않습니다.");
        }
    }

    private Letter findLetter(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException("키워드 편지가 존재하지 않습니다."));
    }
}

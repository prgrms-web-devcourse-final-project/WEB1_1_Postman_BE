package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.LetterNotFoundException;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO) {
        Long userId = 1L;
        String userProfile = "profile url";

        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId, userProfile));
        return LetterResponseDTO.from(letter);
    }

    public Page<LetterHeadersResponseDTO> getLetterHeaders(int page, int size, String sort) {
        Long userId = 1L;

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).descending());
        return letterRepository.findAll(userId, pageable)
                .map(LetterHeadersResponseDTO::from);
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

    public ReceiverDTO getReceiverInfoById(Long letterId) {
        Letter letter = findLetter(letterId);
        return ReceiverDTO.from(letter);
    }
}

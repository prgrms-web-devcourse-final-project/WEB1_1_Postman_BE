package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.SavedLetter;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.exception.LetterAlreadySavedException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SavedLetterService {

    private final SavedLetterRepository savedLetterRepository;
    private final LetterRepository letterRepository;

    @Transactional
    public void saveLetter(Long letterId) {
        Long userId = getCurrentUserId();

        validateCanBeSaved(userId, letterId);
        validateLetterExists(letterId);

        SavedLetter savedLetter = SavedLetter.builder()
                .userId(userId)
                .letterId(letterId)
                .build();

        savedLetterRepository.save(savedLetter);
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getSavedLetterHeaders(int page, int size) {
        Long userId = getCurrentUserId();

        Pageable pageable = PageRequest.of(page - 1, size);
        return savedLetterRepository.findSavedLetters(userId, pageable)
                .map(LetterHeadersResponseDTO::from);
    }

    @Transactional
    public void deleteSavedLetter(Long letterId) {
        Long userId = getCurrentUserId();

        validateSavedLetterExists(letterId, userId);
        savedLetterRepository.remove(userId, letterId);
    }

    private void validateLetterExists(Long letterId) {
        if (!letterRepository.existsById(letterId)) {
            throw new LetterNotFoundException("키워드 편지가 존재하지 않습니다.");
        }
    }

    private void validateCanBeSaved(Long userId, Long letterId) {
        if (savedLetterRepository.isSaved(userId, letterId)) {
            throw new LetterAlreadySavedException("이미 저장된 편지입니다.");
        }
    }

    private void validateSavedLetterExists(Long letterId, Long userId) {
        if (!savedLetterRepository.isSaved(userId, letterId)) {
            throw new LetterNotFoundException("보관된 키워드 편지가 존재하지 않습니다.");
        }
    }

    private Long getCurrentUserId() {
        return 1L; // TODO: 실제 인증 로직으로 대체
    }
}

package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.exception.LetterAlreadySavedException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterBoxService {

    private final LetterBoxRepository letterBoxRepository;
    private final LetterRepository letterRepository;

    @Transactional
    public void saveLetter(Long letterId) {
        Long userId = getCurrentUserId();

        validateCanBeSaved(userId, letterId);
        validateLetterExists(letterId);

        LetterBox letterBox = LetterBox.builder()
                .userId(userId)
                .letterId(letterId)
                .build();

        letterBoxRepository.save(letterBox);
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getSentLetterHeaders(int page, int size, String sort) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getReceivedLetterHeaders(int page, int size, String sort) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getAllLetterHeaders(int page, int size, String sort) {
        Long userId = getCurrentUserId();

        Pageable pageable = PageRequest.of(page - 1, size);
        return letterBoxRepository.findSavedLetters(userId, pageable)
                .map(LetterHeadersResponseDTO::from);
    }

    @Transactional
    public void handleLetterDeletion(Long letterId) {
        Long userId = getCurrentUserId();

        validateSavedLetterExists(letterId, userId);
        letterBoxRepository.remove(userId, letterId);
    }

    private void validateLetterExists(Long letterId) {
        if (!letterRepository.existsById(letterId)) {
            throw new LetterNotFoundException("키워드 편지가 존재하지 않습니다.");
        }
    }

    private void validateCanBeSaved(Long userId, Long letterId) {
        if (letterBoxRepository.isSaved(userId, letterId)) {
            throw new LetterAlreadySavedException("이미 저장된 편지입니다.");
        }
    }

    private void validateSavedLetterExists(Long letterId, Long userId) {
        if (!letterBoxRepository.isSaved(userId, letterId)) {
            throw new LetterNotFoundException("보관된 키워드 편지가 존재하지 않습니다.");
        }
    }

    private Long getCurrentUserId() {
        return 1L; // TODO: 실제 인증 로직으로 대체
    }
}

package postman.bottler.letter.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.LetterAccessDeniedException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;

    @Transactional
    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO) {
        Long userId = getCurrentUserId();
        String userProfile = "profile url";

        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId, userProfile));
        return LetterResponseDTO.from(letter);
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getLetterHeaders(int page, int size, String sort) {
        Long userId = getCurrentUserId();

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(sort).descending());
        return letterRepository.findAll(userId, pageable)
                .map(LetterHeadersResponseDTO::from);
    }

    @Transactional
    public void deleteLetter(Long letterId) {
        Long userId = getCurrentUserId();
        validateLetterOwnership(userId, letterId);
        letterRepository.remove(letterId);
    }

    @Transactional(readOnly = true)
    public LetterResponseDTO getLetterDetail(Long letterId) {
        Letter letter = findLetter(letterId);
        return LetterResponseDTO.from(letter);
    }

    public void blockLetter(Long letterId) {

    }

    @Transactional(readOnly = true)
    public ReceiverDTO getReceiverInfoById(Long letterId) {
        Letter letter = findLetter(letterId);
        return ReceiverDTO.from(letter);
    }

    private void validateLetterOwnership(Long userId, Long letterId) {
        if (!letterRepository.existsByUserIdAndLetterId(userId, letterId)) {
            throw new LetterAccessDeniedException("해당 편지의 작성자가 아닙니다.");
        }
    }

    private Letter findLetter(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException("키워드 편지가 존재하지 않습니다."));
    }

    private Long getCurrentUserId() {
        return 1L; // TODO: 실제 인증 로직으로 대체
    }
}

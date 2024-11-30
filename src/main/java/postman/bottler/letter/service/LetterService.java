package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.LetterBoxDTO;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.dto.response.LetterResponseDTO;
import postman.bottler.letter.exception.LetterAccessDeniedException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final LetterBoxService letterBoxService;

    @Transactional
    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO) {
        Long userId = getCurrentUserId();
        String userProfile = "profile url";

        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId, userProfile));
        letterBoxService.saveLetter(
                LetterBoxDTO.of(userId, letter.getId(), LetterType.LETTER, BoxType.SEND, letter.getCreatedAt())
        );
        return LetterResponseDTO.from(letter);
    }

    @Transactional
    public void deleteLetter(Long letterId) {
        Long userId = getCurrentUserId();
        validateLetterOwnership(userId, letterId);
        letterRepository.delete(letterId);
    }

    @Transactional
    public void deleteLetters(List<Long> letterIds) {
        letterRepository.deleteByIds(letterIds);
    }

    @Transactional(readOnly = true)
    public LetterDetailResponseDTO getLetterDetail(Long letterId) {
        Letter letter = findLetter(letterId);
        Long userId = getCurrentUserId();
        return LetterDetailResponseDTO.from(letter, userId);
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

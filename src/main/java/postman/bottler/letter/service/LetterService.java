package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.LetterBoxDTO;
import postman.bottler.letter.dto.ReceiverDTO;
import postman.bottler.letter.dto.request.LetterRequestDTO;
import postman.bottler.letter.exception.LetterAuthorMismatchException;
import postman.bottler.letter.exception.LetterNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final LetterBoxService letterBoxService;

    @Transactional
    public Letter createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId));
        letterBoxService.saveLetter(
                LetterBoxDTO.of(userId, letter.getId(), LetterType.LETTER, BoxType.SEND, letter.getCreatedAt())
        );
        return letter;
    }

    @Transactional(readOnly = true)
    public Letter findLetter(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException("키워드 편지가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<Letter> findRecommendHeaders(List<Long> letterIds) {
        return letterRepository.findAllActiveByIds(letterIds);
    }

    @Transactional(readOnly = true)
    public ReceiverDTO findReceiverInfoById(Long letterId) {
        return ReceiverDTO.from(findLetter(letterId));
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByUserId(Long userId) {
        return letterRepository.findAllByUserId(userId).stream()
                .map(Letter::getId)
                .toList();
    }

    @Transactional
    public void deleteLetters(List<Long> letterIds, Long userId) {
        letterIds.forEach(letterId -> {
            if (!findLetter(letterId).getUserId().equals(userId)) {
                throw new LetterAuthorMismatchException("요청자와 작성자가 일치하지 않습니다.");
            }
        });
        letterRepository.softDeleteByIds(letterIds);
    }

    @Transactional
    public Long blockLetter(Long letterId) {
        Letter letter = findLetter(letterId);
        letterRepository.softBlockById(letterId);
        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public boolean checkLetterExists(Long letterId) {
        return letterRepository.existsById(letterId);
    }
}

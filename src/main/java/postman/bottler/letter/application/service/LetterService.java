package postman.bottler.letter.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.dto.ReceiverDTO;
import postman.bottler.letter.application.dto.request.LetterRequestDTO;
import postman.bottler.letter.application.repository.LetterRepository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.domain.LetterType;
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
        return findLetterOrThrow(letterId);
    }

    @Transactional(readOnly = true)
    public List<Letter> findRecommendedLetters(List<Long> letterIds) {
        return letterRepository.findAllByIds(letterIds);
    }

    @Transactional(readOnly = true)
    public ReceiverDTO findReceiverInfo(Long letterId) {
        return ReceiverDTO.from(findLetter(letterId));
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByUserId(Long userId) {
        return letterRepository.findAllByUserId(userId).stream()
                .map(Letter::getId)
                .toList();
    }

    @Transactional
    public void softDeleteLetters(List<Long> letterIds, Long userId) {
        List<Letter> letters = letterRepository.findAllByIds(letterIds);
        if (letters.stream().anyMatch(letter -> !letter.getUserId().equals(userId))) {
            throw new LetterAuthorMismatchException("삭제할 편지가 요청자와 일치하지 않습니다.");
        }
        letterRepository.softDeleteByIds(letterIds);
    }

    @Transactional
    public Long softBlockLetter(Long letterId) {
        Letter letter = findLetter(letterId);
        letterRepository.softBlockById(letterId);
        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public boolean letterExists(Long letterId) {
        return letterRepository.existsById(letterId);
    }

    private Letter findLetterOrThrow(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException("키워드 편지가 존재하지 않습니다."));
    }
}

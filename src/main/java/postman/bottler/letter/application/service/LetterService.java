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
        log.info("편지 생성 요청: userId={}, title={}", userId, letterRequestDTO.title());

        Letter letter = letterRequestDTO.toDomain(userId);
        Letter savedLetter = letterRepository.save(letter);
        letterBoxService.saveLetter(LetterBoxDTO.of(userId, savedLetter.getId(), LetterType.LETTER, BoxType.SEND,
                savedLetter.getCreatedAt()));

        log.info("편지 저장 완료: letterId={}, userId={}", savedLetter.getId(), userId);
        return savedLetter;
    }

    @Transactional(readOnly = true)
    public Letter findLetter(Long letterId) {
        log.debug("편지 조회 요청: letterId={}", letterId);

        return letterRepository.findById(letterId).orElseThrow(() -> {
            log.error("편지 조회 실패 (존재하지 않음): letterId={}", letterId);
            return new LetterNotFoundException(LetterType.LETTER);
        });
    }

    @Transactional(readOnly = true)
    public List<Letter> findRecommendedLetters(List<Long> letterIds) {
        log.debug("추천 편지 조회 요청: letterIds={}", letterIds);

        List<Letter> letters = letterRepository.findAllByIds(letterIds);

        if (letters.isEmpty()) {
            log.warn("추천할 편지가 없음: letterIds={}", letterIds);
        } else {
            log.info("추천 편지 조회 성공: count={}", letters.size());
        }

        return letters;
    }

    @Transactional(readOnly = true)
    public ReceiverDTO findReceiverInfo(Long letterId) {
        log.debug("수신자 정보 조회 요청: letterId={}", letterId);

        Letter letter = findLetter(letterId);
        log.info("수신자 정보 조회 완료: receiverId={}", letter.getId());

        return ReceiverDTO.from(letter);
    }

    @Transactional(readOnly = true)
    public List<Long> findIdsByUserId(Long userId) {
        log.debug("사용자 편지 ID 조회 요청: userId={}", userId);

        List<Long> letterIds = letterRepository.findAllByUserId(userId).stream().map(Letter::getId).toList();

        log.info("사용자 편지 ID 조회 완료: userId={}, count={}", userId, letterIds.size());

        return letterIds;
    }

    @Transactional
    public void softDeleteLetters(List<Long> letterIds, Long userId) {
        log.info("편지 삭제 요청: userId={}, letterIds={}", userId, letterIds);

        List<Letter> letters = letterRepository.findAllByIds(letterIds);
        if (letters.stream().anyMatch(letter -> !letter.getUserId().equals(userId))) {
            throw new LetterAuthorMismatchException();
        }

        letterRepository.softDeleteByIds(letterIds);
        log.info("편지 삭제 완료: userId={}, count={}", userId, letterIds.size());
    }

    @Transactional
    public Long softBlockLetter(Long letterId) {
        log.info("편지 차단 요청: letterId={}", letterId);

        Letter letter = findLetter(letterId);
        letterRepository.softBlockById(letter.getId());

        log.info("편지 차단 완료: letterId={}, userId={}", letterId, letter.getUserId());
        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public boolean existsLetterById(Long letterId) {
        log.debug("편지 존재 여부 확인: letterId={}", letterId);

        boolean exists = letterRepository.existsById(letterId);

        log.info("편지 존재 여부: letterId={}, exists={}", letterId, exists);
        return exists;
    }
}

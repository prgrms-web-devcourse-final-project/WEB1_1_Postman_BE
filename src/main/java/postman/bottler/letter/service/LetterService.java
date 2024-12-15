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
import postman.bottler.letter.dto.response.LetterRecommendHeadersResponseDTO;
import postman.bottler.letter.exception.DeveloperLetterException;
import postman.bottler.letter.exception.LetterAuthorMismatchException;
import postman.bottler.letter.exception.LetterNotFoundException;
import postman.bottler.notification.dto.request.NotificationLabelRequestDTO;
import postman.bottler.user.service.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final LetterBoxService letterBoxService;
    private final UserService userService;

    @Transactional
    public Letter createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId));
        letterBoxService.saveLetter(
                LetterBoxDTO.of(userId, letter.getId(), LetterType.LETTER, BoxType.SEND, letter.getCreatedAt())
        );
        return letter;
    }

    @Transactional
    public void createDeveloperLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        List<String> keywords = letterRequestDTO.keywords();
        if (keywords.size() == 1 && "개발자편지".equals(keywords.get(0))) {
            throw new DeveloperLetterException("개발자 편지에는 개발자편지 키워드만 추가할 수 있습니다.");
        }
        letterRepository.save(letterRequestDTO.toDomain(userId));
    }

    @Transactional
    public void deleteLetters(List<Long> letterIds, Long userId) {
        letterIds.forEach(letterId -> {
            if (!findLetter(letterId).getUserId().equals(userId)) {
                throw new LetterAuthorMismatchException("요청자와 작성자가 일치하지 않습니다.");
            }
        });
        letterRepository.deleteByIds(letterIds);
    }

    @Transactional
    public Long blockLetter(Long letterId) {
        Letter letter = findLetter(letterId);
        letterRepository.blockLetterById(letterId);
        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public ReceiverDTO getReceiverInfoById(Long letterId) {
        Letter letter = findLetter(letterId);
        return ReceiverDTO.from(letter);
    }

    @Transactional(readOnly = true)
    public Letter findLetter(Long letterId) {
        return letterRepository.findById(letterId)
                .orElseThrow(() -> new LetterNotFoundException("키워드 편지가 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public List<LetterRecommendHeadersResponseDTO> getRecommendHeaders(List<Long> letterIds) {
        List<Letter> letters = letterRepository.findAllByIds(letterIds);
        return letters.stream()
                .map(LetterRecommendHeadersResponseDTO::from)
                .toList();
    }

    public boolean checkLetterExists(Long letterId) {
        return letterRepository.checkLetterExists(letterId);
    }

    public List<NotificationLabelRequestDTO> getLabels(List<Long> ids) {
        return letterRepository.findAllByIds(ids).stream()
                .map(find -> new NotificationLabelRequestDTO(find.getId(), find.getLabel()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> findAllByUserId(Long userId) {
        return letterRepository.findAllByUserId(userId).stream()
                .map(Letter::getId)
                .toList();
    }
}

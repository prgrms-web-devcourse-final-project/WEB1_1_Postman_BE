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
import postman.bottler.letter.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.dto.response.LetterRecommendHeadersResponseDTO;
import postman.bottler.letter.exception.LetterNotFoundException;
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
    public void deleteLetters(List<Long> letterIds) {
        letterRepository.deleteByIds(letterIds);
    }

    @Transactional(readOnly = true)
    public LetterDetailResponseDTO getLetterDetail(Long letterId, List<String> keywords, Long currentUserId) {
        Letter letter = findLetter(letterId);
        String profile = userService.getProfileImageUrlById(letter.getUserId());
        return LetterDetailResponseDTO.from(letter, keywords, currentUserId, profile);
    }

    @Transactional
    public Long blockLetter(Long letterId) {
        Letter letter = findLetter(letterId);
        letterRepository.blockLetterById(letterId);
        log.info("Letter blocked: {}", letterId);
        return letter.getUserId();
    }

    @Transactional(readOnly = true)
    public ReceiverDTO getReceiverInfoById(Long letterId) {
        Letter letter = findLetter(letterId);
        return ReceiverDTO.from(letter);
    }

    private Letter findLetter(Long letterId) {
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
}

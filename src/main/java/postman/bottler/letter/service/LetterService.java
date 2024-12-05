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
import postman.bottler.letter.exception.LetterNotFoundException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterService {

    private final LetterRepository letterRepository;
    private final LetterBoxService letterBoxService;

    @Transactional
    public Letter createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        String userProfile = "profile url";

        Letter letter = letterRepository.save(letterRequestDTO.toDomain(userId, userProfile));
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
    public LetterDetailResponseDTO getLetterDetail(Long letterId, List<String> keywords, Long userId) {
        Letter letter = findLetter(letterId);
        return LetterDetailResponseDTO.from(letter, keywords, userId);
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
}

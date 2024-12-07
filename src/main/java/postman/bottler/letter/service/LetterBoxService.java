package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.LetterBoxDTO;
import postman.bottler.letter.dto.request.PageRequestDTO;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.exception.UnauthorizedLetterAccessException;

@Slf4j
@Service
@RequiredArgsConstructor
public class LetterBoxService {

    private final LetterBoxRepository letterBoxRepository;

    @Transactional
    public void saveLetter(LetterBoxDTO letterBoxDTO) {
        letterBoxRepository.save(letterBoxDTO.toDomain());
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getAllLetterHeaders(PageRequestDTO pageRequestDTO, Long userId) {
        return letterBoxRepository.findAllLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getSentLetterHeaders(PageRequestDTO pageRequestDTO, Long userId) {
        return letterBoxRepository.findSentLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getReceivedLetterHeaders(PageRequestDTO pageRequestDTO, Long userId) {
        return letterBoxRepository.findReceivedLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional
    public void deleteByIdsAndType(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Transactional
    public void deleteByIdsAndTypeAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId) {
        letterBoxRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);
    }


    @Transactional(readOnly = true)
    public List<Long> getLettersByUserId(Long userId) {
        return letterBoxRepository.getReceivedLettersById(userId);
    }

    @Transactional(readOnly = true)
    public void validateLetterInUserBox(Long letterId, Long userId) {
        boolean isLetterInUserBox = letterBoxRepository.existsByLetterIdAndUserId(letterId, userId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException("사용자가 해당 편지에 접근할 권한이 없습니다.");
        }
    }
}

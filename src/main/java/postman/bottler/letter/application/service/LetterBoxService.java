package postman.bottler.letter.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.dto.request.PageRequestDTO;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.application.repository.LetterBoxRepository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.exception.InvalidLetterRequestException;
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
    public Page<LetterSummaryResponseDTO> findAllLetterSummaries(PageRequestDTO pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.NONE);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponseDTO> findSentLetterSummaries(PageRequestDTO pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.SEND);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponseDTO> findReceivedLetterSummaries(PageRequestDTO pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.RECEIVE);
    }

    @Transactional(readOnly = true)
    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        return letterBoxRepository.findReceivedLetterIdsByUserId(userId);
    }

    @Transactional
    public void deleteByLetterIdsAndType(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Transactional
    public void deleteByLetterIdsAndTypeForUser(List<Long> letterIds, LetterType letterType, BoxType boxType,
                                                Long userId) {
        validateLetterIds(letterIds);
        letterBoxRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);
    }

    @Transactional(readOnly = true)
    public void validateLetterInUserBox(Long letterId, Long userId) {
        boolean isLetterInUserBox = letterBoxRepository.existsByLetterIdAndUserId(letterId, userId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException();
        }
    }

    private Page<LetterSummaryResponseDTO> findLetters(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxRepository.findLetters(userId, pageable, boxType);
    }

    private void validateLetterIds(List<Long> letterIds) {
        if (letterIds == null || letterIds.isEmpty()) {
            throw new InvalidLetterRequestException("삭제할 편지 ID 목록이 비어 있습니다.");
        }
    }
}

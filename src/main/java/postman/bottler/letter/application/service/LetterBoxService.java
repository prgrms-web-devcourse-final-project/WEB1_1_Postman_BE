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
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.UNKNOWN);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponseDTO> findSentLetterSummaries(PageRequestDTO pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.SEND);
    }

    @Transactional(readOnly = true)
    public Page<LetterSummaryResponseDTO> findReceivedLetterSummaries(PageRequestDTO pageRequestDTO, Long userId) {
        return findLetters(userId, pageRequestDTO.toPageable(), BoxType.RECEIVE);
    }

    public Page<LetterSummaryResponseDTO> findLetters(Long userId, Pageable pageable, BoxType boxType) {
        return letterBoxRepository.findLetters(userId, pageable, boxType);
    }

    @Transactional(readOnly = true)
    public List<Long> findReceivedLettersByUserId(Long userId) {
        return letterBoxRepository.findReceivedLettersByUserId(userId);
    }

    @Transactional
    public void deleteByLetterIdsAndType(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Transactional
    public void deleteByLetterIdsAndTypeForUser(List<Long> letterIds, LetterType letterType, BoxType boxType,
                                                Long userId) {
        letterBoxRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);
    }

    @Transactional(readOnly = true)
    public void validateLetterInUserBox(Long letterId, Long userId) {
        boolean isLetterInUserBox = letterBoxRepository.existsByLetterIdAndUserId(letterId, userId);
        if (!isLetterInUserBox) {
            throw new UnauthorizedLetterAccessException("사용자가 해당 편지에 접근할 권한이 없습니다.");
        }
    }
}

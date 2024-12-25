package postman.bottler.letter.applications.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.response.LetterSummaryResponseDTO;

public interface LetterBoxRepository {
    void save(LetterBox letterBox);

    Page<LetterSummaryResponseDTO> findAllLetters(Long userId, Pageable pageable);

    Page<LetterSummaryResponseDTO> findSentLetters(Long userId, Pageable pageable);

    Page<LetterSummaryResponseDTO> findReceivedLetters(Long userId, Pageable pageable);

    List<Long> findReceivedLettersByUserId(Long userId);

    void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType);

    void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId);

    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}

package postman.bottler.letter.application.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

public interface LetterBoxRepository {
    void save(LetterBox letterBox);

    Page<LetterSummaryResponseDTO> findLetters(Long userId, Pageable pageable, BoxType boxType);

    List<Long> findReceivedLetterIdsByUserId(Long userId);

    void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType);

    void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId);

    boolean existsByLetterIdAndUserId(Long letterId, Long userId);
}

package postman.bottler.letter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.application.repository.LetterBoxRepository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LetterBoxRepositoryImpl implements LetterBoxRepository {

    private final LetterBoxQueryRepository letterBoxQueryRepository;
    private final LetterBoxJdbcRepository letterBoxJdbcRepository;

    @Override
    public void save(LetterBox letterBox) {
        letterBoxJdbcRepository.save(letterBox);
    }

    @Override
    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        return letterBoxQueryRepository.findReceivedLetterIdsByUserId(userId);
    }

    @Override
    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxQueryRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    @Override
    public void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId) {
        letterBoxQueryRepository.deleteByConditionAndUserId(letterIds, letterType, boxType, userId);
    }

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return letterBoxJdbcRepository.existsByUserIdAndLetterId(letterId, userId);
    }

    @Override
    public Page<LetterSummaryResponseDTO> findLetters(Long userId, Pageable pageable, BoxType boxType) {
        if (boxType == BoxType.UNKNOWN) {
            boxType = null;
        }
        List<LetterSummaryResponseDTO> letterSummaryResponseDTOS =
                letterBoxQueryRepository.fetchLetters(userId, boxType, pageable);
        long total = countLetters(userId, boxType);
        return new PageImpl<>(letterSummaryResponseDTOS, pageable, total);
    }

    private long countLetters(Long userId, BoxType boxType) {
        return letterBoxQueryRepository.countLetters(userId, boxType);
    }
}

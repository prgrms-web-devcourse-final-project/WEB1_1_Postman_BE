package postman.bottler.letter.infra;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.service.LetterBoxRepository;

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
    public Page<LetterSummaryResponseDTO> findAllLetters(Long userId, Pageable pageable) {
        List<LetterSummaryResponseDTO> results = fetchLetters(userId, null, pageable);
        long total = countLetters(userId, null);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterSummaryResponseDTO> findSentLetters(Long userId, Pageable pageable) {
        List<LetterSummaryResponseDTO> results = fetchLetters(userId, BoxType.SEND, pageable);
        long total = countLetters(userId, BoxType.SEND);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterSummaryResponseDTO> findReceivedLetters(Long userId, Pageable pageable) {
        List<LetterSummaryResponseDTO> results = fetchLetters(userId, BoxType.RECEIVE, pageable);
        long total = countLetters(userId, BoxType.RECEIVE);
        return new PageImpl<>(results, pageable, total);
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
    public List<Long> findReceivedLettersById(Long userId) {
        return letterBoxQueryRepository.getReceivedLettersById(userId);
    }

    @Override
    public boolean existsByLetterIdAndUserId(Long letterId, Long userId) {
        return letterBoxJdbcRepository.existsByUserIdAndLetterId(letterId, userId);
    }

    private List<LetterSummaryResponseDTO> fetchLetters(Long userId, BoxType boxType, Pageable pageable) {
        return letterBoxQueryRepository.fetchLetters(userId, boxType, pageable);
    }

    private long countLetters(Long userId, BoxType boxType) {
        return letterBoxQueryRepository.countLetters(userId, boxType);
    }
}

package postman.bottler.letter.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;

public interface LetterBoxRepository {
    void save(LetterBox letterBox);

    Page<LetterHeadersResponseDTO> findAllLetters(Long userId, Pageable pageable);

    Page<LetterHeadersResponseDTO> findSentLetters(Long userId, Pageable pageable);

    Page<LetterHeadersResponseDTO> findReceivedLetters(Long userId, Pageable pageable);

    void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType);

    List<Long> getReceivedLettersById(Long userId);
}

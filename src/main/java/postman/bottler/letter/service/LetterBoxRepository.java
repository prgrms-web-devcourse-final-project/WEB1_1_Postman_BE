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

    boolean isSaved(Long userId, Long letterId);

    void remove(Long userId, Long letterId);

    Page<LetterHeadersResponseDTO> findAllLetters(Long userId, Pageable pageable);

    Page<LetterHeadersResponseDTO> findSentLetters(Long userId, Pageable pageable);

    Page<LetterHeadersResponseDTO> findReceivedLetters(Long userId, Pageable pageable);

    void deleteByIdsAndTypes(List<Long> letterIds, LetterType letterType, BoxType boxType);

    void deleteByIdAndType(Long letterId, LetterType letterType);

    void deleteByIdsAndType(List<Long> letterIds, LetterType letterType);

    void deleteByIdAndTypes(Long letterId, LetterType letterType, BoxType boxType);
}
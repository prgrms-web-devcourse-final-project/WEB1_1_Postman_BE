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
    public Page<LetterHeadersResponseDTO> getAllLetterHeaders(PageRequestDTO pageRequestDTO) {
        Long userId = getCurrentUserId();
        return letterBoxRepository.findAllLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getSentLetterHeaders(PageRequestDTO pageRequestDTO) {
        Long userId = getCurrentUserId();
        return letterBoxRepository.findSentLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional(readOnly = true)
    public Page<LetterHeadersResponseDTO> getReceivedLetterHeaders(PageRequestDTO pageRequestDTO) {
        Long userId = getCurrentUserId();
        return letterBoxRepository.findReceivedLetters(userId, pageRequestDTO.toPageable());
    }

    @Transactional
    public void deleteByIdsAndType(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        letterBoxRepository.deleteByCondition(letterIds, letterType, boxType);
    }

    private Long getCurrentUserId() {
        return 1L; // TODO: 실제 인증 로직으로 대체
    }
}

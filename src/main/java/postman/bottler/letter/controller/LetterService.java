package postman.bottler.letter.controller;

import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;

import java.util.List;

public interface LetterService {
    //편지 블락 처리 로직 편지 블락 시키고 3번 되면 정지요청만 보내기
    void incrementWarningCount(Long letterId);
    Letter createLetter(LetterRequestDTO request);
    List<Letter> getSentLetters(Pageable pageable);
    Letter getLetterDetail(Long letterId);
    void saveLetter(Long letterId);
    List<Letter> getSavedLetters(Pageable pageable);
    void deleteSavedLetter(Long letterId);
}

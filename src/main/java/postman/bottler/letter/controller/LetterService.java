package postman.bottler.letter.controller;

import org.springframework.data.domain.Pageable;
import postman.bottler.letter.domain.Letter;
import postman.bottler.letter.dto.request.LetterRequestDTO;

import java.util.List;

public interface LetterService {
    //편지 블락 처리 로직
    Letter createLetter(LetterRequestDTO request);
    List<Letter> getSentLetters(Pageable pageable);
    Letter getLetterDetail(Long letterId);
    void saveLetter(Long letterId);
    List<Letter> getSavedLetters(Pageable pageable);
    void deleteSavedLetter(Long letterId);
}

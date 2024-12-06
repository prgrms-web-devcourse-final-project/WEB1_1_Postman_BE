package postman.bottler.letter.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.letter.domain.Letter;

@Service
@RequiredArgsConstructor
public class LetterHelperService {

    private final LetterRepository letterRepository;

    public List<Letter> getRecommendedLetters(List<Long> letterIds) {
        return letterRepository.findAllByIds(letterIds);
    }
}

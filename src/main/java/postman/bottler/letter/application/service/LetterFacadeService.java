package postman.bottler.letter.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.service.LetterKeywordService;
import postman.bottler.keyword.application.service.RedisLetterService;
import postman.bottler.keyword.domain.LetterKeyword;
import postman.bottler.letter.application.dto.request.LetterRequestDTO;
import postman.bottler.letter.application.dto.response.LetterDetailResponseDTO;
import postman.bottler.letter.application.dto.response.LetterRecommendSummaryResponseDTO;
import postman.bottler.letter.application.dto.response.LetterResponseDTO;
import postman.bottler.letter.domain.Letter;
import postman.bottler.user.application.service.UserService;

@Service
@RequiredArgsConstructor
public class LetterFacadeService {

    private final LetterBoxService letterBoxService;
    private final LetterService letterService;
    private final LetterKeywordService letterKeywordService;
    private final RedisLetterService redisLetterService;
    private final UserService userService;
    private final ReplyLetterService replyLetterService;

    @Transactional
    public LetterResponseDTO createLetter(LetterRequestDTO letterRequestDTO, Long userId) {
        Letter letter = letterService.createLetter(letterRequestDTO, userId);
        List<LetterKeyword> keywords = letterKeywordService.createLetterKeywords(
                letter.getId(),
                letterRequestDTO.keywords()
        );
        return LetterResponseDTO.from(letter, keywords);
    }

    @Transactional(readOnly = true)
    public LetterDetailResponseDTO findLetterDetail(Long letterId, Long currentUserId) {
        letterBoxService.validateLetterInUserBox(letterId, currentUserId);
        boolean isReplied = replyLetterService.checkIsReplied(letterId, currentUserId);
        List<LetterKeyword> keywords = letterKeywordService.getKeywords(letterId);
        String profile = userService.getProfileImageUrlById(currentUserId);
        Letter letter = letterService.findLetter(letterId);
        return LetterDetailResponseDTO.from(letter, keywords, currentUserId, profile, isReplied);
    }

    @Transactional(readOnly = true)
    public List<LetterRecommendSummaryResponseDTO> findRecommendHeaders(Long userId) {
        List<Long> letterIds = redisLetterService.getRecommendations(userId);
        List<Letter> letters = letterService.findRecommendedLetters(letterIds);
        return letters.stream()
                .map(LetterRecommendSummaryResponseDTO::from)
                .toList();
    }
}

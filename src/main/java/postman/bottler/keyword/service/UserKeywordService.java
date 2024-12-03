package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.dto.response.UserKeywordResponseDTO;

@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;

    public UserKeywordResponseDTO getKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordRepository.findAllByUserId(userId);
        return UserKeywordResponseDTO.from(userKeywords);
    }
}

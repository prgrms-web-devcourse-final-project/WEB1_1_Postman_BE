package postman.bottler.keyword.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.dto.request.UserKeywordRequestDTO;
import postman.bottler.keyword.dto.response.UserKeywordResponseDTO;

@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;

    @Transactional(readOnly = true)
    public UserKeywordResponseDTO getKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordRepository.findAllByUserId(userId);
        return UserKeywordResponseDTO.from(userKeywords);
    }

    @Transactional
    public void createKeywords(UserKeywordRequestDTO userKeywordRequestDTO, Long userId) {
        userKeywordRepository.deleteAllByUserId(userId);
        userKeywordRepository.saveAll(userKeywordRequestDTO.toDomain(userId));
    }
}

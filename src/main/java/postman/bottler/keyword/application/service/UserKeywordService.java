package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.repository.UserKeywordRepository;
import postman.bottler.keyword.domain.UserKeyword;
import postman.bottler.keyword.application.dto.request.UserKeywordRequestDTO;
import postman.bottler.keyword.application.dto.response.UserKeywordResponseDTO;

@Service
@RequiredArgsConstructor
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;

    @Transactional(readOnly = true)
    public UserKeywordResponseDTO getUserKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordRepository.findAllByUserId(userId);
        return UserKeywordResponseDTO.from(userKeywords);
    }

    @Transactional
    public void createKeywords(UserKeywordRequestDTO userKeywordRequestDTO, Long userId) {
        userKeywordRepository.replaceAllByUserId(userKeywordRequestDTO.toDomain(userId), userId);
    }

    @Transactional(readOnly = true)
    public List<String> getKeywordsByUserId(Long userId) {
        return userKeywordRepository.findKeywordsByUserId(userId);
    }
}

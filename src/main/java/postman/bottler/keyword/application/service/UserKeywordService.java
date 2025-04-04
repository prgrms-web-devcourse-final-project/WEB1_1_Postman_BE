package postman.bottler.keyword.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.keyword.application.dto.request.UserKeywordRequestDTO;
import postman.bottler.keyword.application.dto.response.UserKeywordResponseDTO;
import postman.bottler.keyword.application.repository.UserKeywordRepository;
import postman.bottler.keyword.domain.UserKeyword;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserKeywordService {

    private final UserKeywordRepository userKeywordRepository;

    @Transactional(readOnly = true)
    public UserKeywordResponseDTO findUserKeywords(Long userId) {
        List<UserKeyword> userKeywords = userKeywordRepository.findUserKeywordsByUserId(userId);
        return UserKeywordResponseDTO.from(userKeywords);
    }

    @Transactional
    public void createKeywords(UserKeywordRequestDTO userKeywordRequestDTO, Long userId) {
        List<UserKeyword> userKeywords = userKeywordRequestDTO.toDomain(userId);
        userKeywordRepository.replaceKeywordsByUserId(userKeywords, userId);
    }

    @Transactional(readOnly = true)
    public List<String> findKeywords(Long userId) {
        return userKeywordRepository.findKeywordsByUserId(userId);
    }
}

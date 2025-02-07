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
        log.debug("[사용자 키워드 조회 요청] userId={}", userId);

        List<UserKeyword> userKeywords = userKeywordRepository.findUserKeywordsByUserId(userId);
        log.info("[사용자 키워드 조회 완료] userId={}, 키워드 개수={}", userId, userKeywords.size());

        return UserKeywordResponseDTO.from(userKeywords);
    }

    @Transactional
    public void createKeywords(UserKeywordRequestDTO userKeywordRequestDTO, Long userId) {
        log.info("[사용자 키워드 생성 요청] userId={}, 키워드 개수={}", userId, userKeywordRequestDTO.keywords().size());

        List<UserKeyword> userKeywords = userKeywordRequestDTO.toDomain(userId);
        userKeywordRepository.replaceKeywordsByUserId(userKeywords, userId);

        log.info("[사용자 키워드 저장 완료] userId={}, 저장된 키워드 개수={}", userId, userKeywords.size());
    }

    @Transactional(readOnly = true)
    public List<String> findKeywords(Long userId) {
        log.debug("[사용자 키워드 문자열 조회 요청] userId={}", userId);

        List<String> keywords = userKeywordRepository.findKeywordsByUserId(userId);

        log.info("[사용자 키워드 문자열 조회 완료] userId={}, 키워드 개수={}", userId, keywords.size());

        return keywords;
    }
}

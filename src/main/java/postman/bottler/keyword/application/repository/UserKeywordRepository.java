package postman.bottler.keyword.application.repository;

import java.util.List;
import postman.bottler.keyword.domain.UserKeyword;

public interface UserKeywordRepository {
    List<UserKeyword> findUserKeywordsByUserId(Long userId);

    void replaceKeywordsByUserId(List<UserKeyword> userKeywords, Long userId);

    List<String> findKeywordsByUserId(Long userId);
}

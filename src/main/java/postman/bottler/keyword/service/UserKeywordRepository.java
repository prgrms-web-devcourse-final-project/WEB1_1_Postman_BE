package postman.bottler.keyword.service;

import java.util.List;
import postman.bottler.keyword.domain.UserKeyword;

public interface UserKeywordRepository {
    List<UserKeyword> findAllByUserId(Long userId);
}

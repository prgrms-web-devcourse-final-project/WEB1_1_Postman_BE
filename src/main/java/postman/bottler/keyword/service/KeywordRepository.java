package postman.bottler.keyword.service;

import java.util.List;
import postman.bottler.keyword.domain.Keyword;

public interface KeywordRepository {

    List<Keyword> getKeywords();
}

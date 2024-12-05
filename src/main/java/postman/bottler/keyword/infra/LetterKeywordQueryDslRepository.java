package postman.bottler.keyword.infra;

import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import postman.bottler.keyword.infra.entity.LetterKeywordEntity;
import postman.bottler.keyword.infra.entity.QLetterKeywordEntity;
import postman.bottler.letter.infra.entity.QLetterEntity;

@Repository
@RequiredArgsConstructor
public class LetterKeywordQueryDslRepository {

    private final JPAQueryFactory queryFactory;

    public List<LetterKeywordEntity> findKeywordsByLetterId(Long letterId) {
        QLetterKeywordEntity letterKeywordEntity = QLetterKeywordEntity.letterKeywordEntity;

        return queryFactory
                .selectFrom(letterKeywordEntity)
                .where(letterKeywordEntity.letterId.eq(letterId))
                .fetch();
    }

    public List<Long> getMatchedLetters(List<String> userKeywords, List<Long> letterIds, int limit) {
        QLetterKeywordEntity qLetterKeyword = QLetterKeywordEntity.letterKeywordEntity;

        List<Long> matchedLetters = queryFactory
                .select(qLetterKeyword.letterId)
                .from(qLetterKeyword)
                .where(qLetterKeyword.keyword.in(userKeywords)
                        .and(qLetterKeyword.letterId.notIn(letterIds))
                        .and(qLetterKeyword.isDeleted.eq(false)))
                .groupBy(qLetterKeyword.letterId)
                .orderBy(qLetterKeyword.letterId.count().desc())
                .limit(limit)
                .fetch();

        if (matchedLetters.size() < limit) {
            int remaining = limit - matchedLetters.size();
            List<Long> randomLetters = getRandomLetters(remaining, letterIds);
            matchedLetters.addAll(randomLetters);
        }

        return matchedLetters;
    }

    private List<Long> getRandomLetters(int limit, List<Long> excludedLetterIds) {
        QLetterEntity qLetter = QLetterEntity.letterEntity;

        return queryFactory
                .select(qLetter.id)
                .from(qLetter)
                .where(qLetter.id.notIn(excludedLetterIds)
                        .and(qLetter.isDeleted.eq(false)))
                .orderBy(Expressions.stringTemplate("function('RAND')").asc())
                .limit(limit)
                .fetch();
    }
}

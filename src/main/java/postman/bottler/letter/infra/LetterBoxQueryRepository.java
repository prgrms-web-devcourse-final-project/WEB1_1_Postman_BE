package postman.bottler.letter.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.application.dto.response.LetterSummaryResponseDTO;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.infra.entity.QLetterBoxEntity;
import postman.bottler.letter.infra.entity.QLetterEntity;
import postman.bottler.letter.infra.entity.QReplyLetterEntity;

@Repository
@RequiredArgsConstructor
public class LetterBoxQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<LetterSummaryResponseDTO> fetchLetters(Long userId, BoxType boxType, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        return queryFactory
                .select(Projections.constructor(
                        LetterSummaryResponseDTO.class,
                        letterBox.letterId,
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.title)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.title)
                                .otherwise("Unknown Title"),
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.label)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.label)
                                .otherwise("Unknown Label"),
                        letterBox.letterType,
                        letterBox.boxType,
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBox.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBox.letterType.eq(LetterType.REPLY_LETTER)))
                .where(letterBox.userId.eq(userId)
                        .and(boxType != BoxType.NONE ? letterBox.boxType.eq(boxType) : null))
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    public List<Long> findReceivedLetterIdsByUserId(Long userId) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        return queryFactory
                .select(letterBox.letterId)
                .from(letterBox)
                .where(letterBox.userId.eq(userId)
                        .and(letterBox.boxType.eq(BoxType.RECEIVE)))
                .fetch();
    }

    public long countLetters(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        return queryFactory
                .select(letterBox.id)
                .from(letterBox)
                .where(letterBox.userId.eq(userId)
                        .and(boxType != BoxType.NONE ? letterBox.boxType.eq(boxType) : null))
                .fetch()
                .size();
    }

    public void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;

        queryFactory.delete(letterBox)
                .where(
                        letterIds != null ? letterBox.letterId.in(letterIds) : null,
                        letterType != LetterType.NONE ? letterBox.letterType.eq(letterType) : null,
                        boxType != BoxType.NONE ? letterBox.boxType.eq(boxType) : null
                )
                .execute();
    }

    public void deleteByConditionAndUserId(List<Long> letterIds, LetterType letterType, BoxType boxType, Long userId) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;

        queryFactory.delete(letterBox)
                .where(
                        letterIds != null ? letterBox.letterId.in(letterIds) : null,
                        letterType != LetterType.NONE ? letterBox.letterType.eq(letterType) : null,
                        boxType != BoxType.NONE ? letterBox.boxType.eq(boxType) : null,
                        letterBox.userId.eq(userId)
                )
                .execute();
    }
}

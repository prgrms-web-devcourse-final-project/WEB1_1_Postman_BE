package postman.bottler.letter.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import postman.bottler.letter.domain.BoxType;
import postman.bottler.letter.domain.LetterBox;
import postman.bottler.letter.domain.LetterType;
import postman.bottler.letter.dto.response.LetterHeadersResponseDTO;
import postman.bottler.letter.infra.entity.LetterBoxEntity;
import postman.bottler.letter.infra.entity.QLetterBoxEntity;
import postman.bottler.letter.infra.entity.QLetterEntity;
import postman.bottler.letter.infra.entity.QReplyLetterEntity;
import postman.bottler.letter.service.LetterBoxRepository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LetterBoxRepositoryImpl implements LetterBoxRepository {

    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;

    @Override
    public void save(LetterBox letterBox) {
        LetterBoxEntity letterBoxEntity = LetterBoxEntity.from(letterBox);
        entityManager.persist(letterBoxEntity);
    }

    @Override
    public Page<LetterHeadersResponseDTO> findAllLetters(Long userId, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        List<LetterHeadersResponseDTO> results = queryFactory
                .select(Projections.constructor(
                        LetterHeadersResponseDTO.class,
                        letterBox.letterId,
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.title)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.title)
                                .otherwise("Unknown Title"),
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.label)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.label)
                                .otherwise("Unknown Label"),
                        letterBox.letterType, // Enum 타입 매핑
                        letterBox.boxType, // Enum 타입 매핑
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBox.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBox.letterType.eq(LetterType.REPLY_LETTER)))
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse()))
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(letterBox)
                .from(letterBox)
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse()))
                .fetch()
                .size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterHeadersResponseDTO> findSentLetters(Long userId, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        List<LetterHeadersResponseDTO> results = queryFactory
                .select(Projections.constructor(
                        LetterHeadersResponseDTO.class,
                        letterBox.letterId,
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.title)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.title)
                                .otherwise(""),
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.label)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.label)
                                .otherwise(""),
                        letterBox.letterType, // Enum 타입 매핑
                        letterBox.boxType, // Enum 타입 매핑
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBox.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBox.letterType.eq(LetterType.REPLY_LETTER)))
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse())
                        .and(letterBox.boxType.eq(BoxType.SEND)))
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(letterBox)
                .from(letterBox)
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse()))
                .fetch()
                .size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterHeadersResponseDTO> findReceivedLetters(Long userId, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        List<LetterHeadersResponseDTO> results = queryFactory
                .select(Projections.constructor(
                        LetterHeadersResponseDTO.class,
                        letterBox.letterId,
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.title)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.title)
                                .otherwise(""),
                        new CaseBuilder()
                                .when(letterBox.letterType.eq(LetterType.LETTER)).then(letter.label)
                                .when(letterBox.letterType.eq(LetterType.REPLY_LETTER)).then(replyLetter.label)
                                .otherwise(""),
                        letterBox.letterType, // Enum 타입 매핑
                        letterBox.boxType, // Enum 타입 매핑
                        letterBox.createdAt
                ))
                .from(letterBox)
                .leftJoin(letter).on(letterBox.letterId.eq(letter.id)
                        .and(letterBox.letterType.eq(LetterType.LETTER)))
                .leftJoin(replyLetter).on(letterBox.letterId.eq(replyLetter.id)
                        .and(letterBox.letterType.eq(LetterType.REPLY_LETTER)))
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse())
                        .and(letterBox.boxType.eq(BoxType.RECEIVE)))
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(letterBox)
                .from(letterBox)
                .where(letterBox.userId.eq(userId).and(letterBox.isDeleted.isFalse()))
                .fetch()
                .size();

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public void deleteAllByLetterIds(List<Long> letterIds, LetterType letterType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        queryFactory.delete(letterBox)
                .where(
                        letterBox.letterId.in(letterIds)
                                .and(letterBox.letterType.eq(letterType))
                )
                .execute();
    }

    @Override
    public void deleteByLetterIds(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        queryFactory.delete(letterBox)
                .where(
                        letterBox.letterId.in(letterIds)
                                .and(letterBox.letterType.eq(letterType))
                                .and(letterBox.boxType.eq(boxType))
                )
                .execute();
    }

    @Override
    public void deleteByLetterId(Long letterId) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        queryFactory.delete(letterBox)
                .where(letterBox.letterId.eq(letterId));
    }

    @Override
    public boolean isSaved(Long userId, Long letterId) {
        return true;
    }

    @Override
    public void remove(Long userId, Long letterId) {
//        letterBoxJpaRepository.deleteByUserIdAndLetterId(userId, letterId);
    }
}

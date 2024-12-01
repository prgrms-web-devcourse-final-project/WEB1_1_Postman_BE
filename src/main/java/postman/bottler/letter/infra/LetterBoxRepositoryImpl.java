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
        List<LetterHeadersResponseDTO> results = fetchLetters(userId, null, pageable);
        long total = countLetters(userId, null);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterHeadersResponseDTO> findSentLetters(Long userId, Pageable pageable) {
        List<LetterHeadersResponseDTO> results = fetchLetters(userId, BoxType.SEND, pageable);
        long total = countLetters(userId, BoxType.SEND);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<LetterHeadersResponseDTO> findReceivedLetters(Long userId, Pageable pageable) {
        List<LetterHeadersResponseDTO> results = fetchLetters(userId, BoxType.RECEIVE, pageable);
        long total = countLetters(userId, BoxType.RECEIVE);
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public void deleteByIdsAndType(List<Long> letterIds, LetterType letterType) {
        deleteByCondition(letterIds, letterType, null);
    }

    @Override
    public void deleteByIdsAndTypes(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        deleteByCondition(letterIds, letterType, boxType);
    }

    @Override
    public void deleteByIdAndType(Long letterId, LetterType letterType) {
        deleteByCondition(List.of(letterId), letterType, null);
    }

    @Override
    public void deleteByIdAndTypes(Long letterId, LetterType letterType, BoxType boxType) {
        deleteByCondition(List.of(letterId), letterType, boxType);
    }

    @Override
    public boolean isSaved(Long userId, Long letterId) {
        return true;
    }

    @Override
    public void remove(Long userId, Long letterId) {
//        letterBoxJpaRepository.deleteByUserIdAndLetterId(userId, letterId);
    }

    private List<LetterHeadersResponseDTO> fetchLetters(Long userId, BoxType boxType, Pageable pageable) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        QLetterEntity letter = QLetterEntity.letterEntity;
        QReplyLetterEntity replyLetter = QReplyLetterEntity.replyLetterEntity;

        return queryFactory
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
                        .and(letterBox.isDeleted.isFalse())
                        .and(boxType != null ? letterBox.boxType.eq(boxType) : null))
                .orderBy(letterBox.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private long countLetters(Long userId, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;
        return queryFactory
                .select(letterBox.id)
                .from(letterBox)
                .where(letterBox.userId.eq(userId)
                        .and(letterBox.isDeleted.isFalse())
                        .and(boxType != null ? letterBox.boxType.eq(boxType) : null))
                .fetch()
                .size();
    }

    private void deleteByCondition(List<Long> letterIds, LetterType letterType, BoxType boxType) {
        QLetterBoxEntity letterBox = QLetterBoxEntity.letterBoxEntity;

        queryFactory.delete(letterBox)
                .where(
                        letterIds != null ? letterBox.letterId.in(letterIds) : null,
                        letterType != null ? letterBox.letterType.eq(letterType) : null,
                        boxType != null ? letterBox.boxType.eq(boxType) : null
                )
                .execute();
    }
}

package postman.bottler.mapletter.infra;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY) //h2 사용
@Transactional
class MapLetterJpaRepositoryTest {

    @Autowired
    private MapLetterJpaRepository mapLetterJpaRepository;

    @BeforeEach
    void setUp() {
        List<MapLetterEntity> mapLetters = List.of(
                MapLetterEntity.builder()
                        .title("Letter 1")
                        .content("Content 1")
                        .latitude(new BigDecimal("37.5665"))
                        .longitude(new BigDecimal("127.23456"))
                        .font("맑은고딕")
                        .paper("www.paper1.com")
                        .label("www.label1.com")
                        .type(MapLetterType.PUBLIC)
                        .targetUserId(2L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(),
                MapLetterEntity.builder()
                        .title("Letter 2")
                        .content("Content 2")
                        .latitude(new BigDecimal("37.5665"))
                        .longitude(new BigDecimal("127.23456"))
                        .font("프리텐다드")
                        .paper("www.paper2.com")
                        .label("www.label2.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(null)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(),
                MapLetterEntity.builder()
                        .title("Letter 3")
                        .content("Content 3")
                        .latitude(new BigDecimal("12.1234"))
                        .longitude(new BigDecimal("127.34567"))
                        .font("굴림체")
                        .paper("www.paper3.com")
                        .label("www.label3.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(2L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(true) // 삭제된 편지
                        .isBlocked(false)
                        .build(),
                MapLetterEntity.builder()
                        .title("Letter 4")
                        .content("Content 4")
                        .latitude(new BigDecimal("37.1111"))
                        .longitude(new BigDecimal("128.2222"))
                        .font("맑은고딕")
                        .paper("www.paper4.com")
                        .label("www.label4.com")
                        .type(MapLetterType.PUBLIC)
                        .targetUserId(null)
                        .createUserId(2L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build()
        );

        mapLetterJpaRepository.saveAll(mapLetters);
    }

    @Test
    @DisplayName("createUserId가 특정 userId이고 삭제되지 않은 편지 조회에 성공한다.")
    void findActiveByCreateUserId() {
        // given
        Long userId = 1L;

        // when
        List<MapLetterEntity> result = mapLetterJpaRepository.findActiveByCreateUserId(userId);

        // then
        assertNotNull(result);
        assertEquals(2, result.size()); // 삭제되지 않은 편지만 반환
        assertTrue(result.stream().noneMatch(MapLetterEntity::isDeleted));
        assertTrue(result.stream().allMatch(letter -> letter.getCreateUserId().equals(userId)));
    }

    @Test
    @DisplayName("targetUserId가 특정 userId이고 삭제되지 않은 편지 조회")
    void findActiveByTargetUserId() {
        // given
        Long targetUserId = 2L;

        // when
        List<MapLetterEntity> result = mapLetterJpaRepository.findActiveByTargetUserId(targetUserId);

        // then
        assertNotNull(result);
        assertEquals(1, result.size()); // 예상되는 편지 개수
        assertTrue(result.stream().noneMatch(MapLetterEntity::isDeleted)); // 삭제되지 않은 편지만 조회되는지 확인
        assertTrue(result.stream().allMatch(letter -> letter.getTargetUserId().equals(targetUserId))); // targetUserId 확인
    }
}
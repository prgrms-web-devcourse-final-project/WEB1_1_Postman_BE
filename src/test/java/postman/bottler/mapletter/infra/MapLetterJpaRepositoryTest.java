package postman.bottler.mapletter.infra;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import postman.bottler.mapletter.domain.MapLetterType;
import postman.bottler.mapletter.dto.MapLetterAndDistance;
import postman.bottler.mapletter.infra.entity.MapLetterEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //로컬 mysql로 테스트 db 변경
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
                        .longitude(new BigDecimal("126.977"))
                        .font("맑은고딕")
                        .paper("www.paper1.com")
                        .label("www.label1.com")
                        .type(MapLetterType.PUBLIC)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(), //퍼블릭, 조회 가능
                MapLetterEntity.builder()
                        .title("Letter 2")
                        .content("Content 2")
                        .latitude(new BigDecimal("50.5665"))
                        .longitude(new BigDecimal("120.23456"))
                        .font("맑은고딕")
                        .paper("www.paper1.com")
                        .label("www.label1.com")
                        .type(MapLetterType.PUBLIC)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(), //퍼블릭, 거리 멀어서 조회 불가
                MapLetterEntity.builder()
                        .title("Letter 3")
                        .content("Content 3")
                        .latitude(new BigDecimal("50.5665"))
                        .longitude(new BigDecimal("120.23456"))
                        .font("맑은고딕")
                        .paper("www.paper1.com")
                        .label("www.label1.com")
                        .type(MapLetterType.PUBLIC)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(true)
                        .isBlocked(false)
                        .build(), //퍼블릭, 삭제된 편지
                MapLetterEntity.builder()
                        .title("Letter 4")
                        .content("Content 4")
                        .latitude(new BigDecimal("37.5665"))
                        .longitude(new BigDecimal("127.23456"))
                        .font("프리텐다드")
                        .paper("www.paper2.com")
                        .label("www.label2.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(3L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(), //타겟, 타겟 유저가 다름
                MapLetterEntity.builder()
                        .title("Letter 5")
                        .content("Content 5")
                        .latitude(new BigDecimal("37.5665"))
                        .longitude(new BigDecimal("126.977"))
                        .font("굴림체")
                        .paper("www.paper3.com")
                        .label("www.label3.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(2L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(), //타겟. 조회 가능
                MapLetterEntity.builder()
                        .title("Letter 6")
                        .content("Content 6")
                        .latitude(new BigDecimal("12.1234"))
                        .longitude(new BigDecimal("127.34567"))
                        .font("굴림체")
                        .paper("www.paper3.com")
                        .label("www.label3.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(7L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(true)
                        .isBlocked(false)
                        .build(), //타겟. 타겟 유저가 다름
                MapLetterEntity.builder()
                        .title("Letter 7")
                        .content("Content 7")
                        .latitude(new BigDecimal("20.1234"))
                        .longitude(new BigDecimal("100.34567"))
                        .font("굴림체")
                        .paper("www.paper3.com")
                        .label("www.label3.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(2L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build(), //타겟. 거리 멀어서 조회 불가능
                MapLetterEntity.builder()
                        .title("Letter 8")
                        .content("Content 8")
                        .latitude(new BigDecimal("12.1234"))
                        .longitude(new BigDecimal("127.34567"))
                        .font("굴림체")
                        .paper("www.paper3.com")
                        .label("www.label3.com")
                        .type(MapLetterType.PRIVATE)
                        .targetUserId(7L)
                        .createUserId(1L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(true)
                        .isBlocked(false)
                        .build(), //타겟, 삭제된 편지
                MapLetterEntity.builder()
                        .title("Letter 9")
                        .content("Content 9")
                        .latitude(new BigDecimal("37.1111"))
                        .longitude(new BigDecimal("128.2222"))
                        .font("맑은고딕")
                        .paper("www.paper4.com")
                        .label("www.label4.com")
                        .type(MapLetterType.PUBLIC)
                        .targetUserId(7L)
                        .createUserId(2L)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .isDeleted(false)
                        .isBlocked(false)
                        .build() //내가 타겟이 아님. 거리 멀어서 조회 불가
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
        assertEquals(5, result.size()); // 삭제되지 않은 편지만 반환
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
        assertEquals(2, result.size()); // 예상되는 편지 개수
        assertTrue(result.stream().noneMatch(MapLetterEntity::isDeleted)); // 삭제되지 않은 편지만 조회되는지 확인
        assertTrue(result.stream().allMatch(letter -> letter.getTargetUserId().equals(targetUserId))); // targetUserId 확인
    }

    @Test
    @DisplayName("반경 500m의 PUBLIC 편지와 내가 타겟인 TARGET 편지 조회에 성공한다.")
    void testFindLettersByUserLocation() {
        //given
        BigDecimal latitude = new BigDecimal("37.5665"); // 기준 위도
        BigDecimal longitude = new BigDecimal("126.978"); // 기준 경도
        Long targetUserId = 2L; // 조회할 타겟 유저 ID

        //when
        List<MapLetterAndDistance> result = mapLetterJpaRepository.findLettersByUserLocation(latitude, longitude, targetUserId);

        //then
        assertNotNull(result);

        assertTrue(result.stream().anyMatch(letter ->
                (letter.getTargetUserId() == null // 퍼블릭 편지
                        || (letter.getTargetUserId() != null && letter.getTargetUserId().equals(targetUserId))) // 타겟 편지
                        && letter.getDistance().compareTo(new BigDecimal("500")) <= 0 // 거리 조건
        ));

        //삭제되거나 조건에 맞지 않는 편지 제외
        assertTrue(result.stream().noneMatch(letter ->
                letter.getTitle().equals("Letter 2")
        ));

        assertTrue(result.stream().anyMatch(letter -> letter.getTitle().equals("Letter 1")), "Letter 1이 조회 결과에 포함되어야 합니다.");
        assertTrue(result.stream().anyMatch(letter -> letter.getTitle().equals("Letter 5")), "Letter 5이 조회 결과에 포함되어야 합니다.");

        assertEquals(2, result.size());
    }
}

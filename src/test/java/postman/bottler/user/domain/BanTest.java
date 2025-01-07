package postman.bottler.user.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 정지 도메인 테스트")
class BanTest {

    private Long testUserId;
    private Long testBanDuration;

    @BeforeEach
    void setUp() {
        testUserId = 1L;
        testBanDuration = 7L;
    }

    @Test
    @DisplayName("Ban 객체 생성")
    public void createBan() {
        //when
        Ban ban = Ban.create(testUserId, testBanDuration);

        //then
        assertNotNull(ban);
        assertNotNull(ban.getBannedAt());
        assertNotNull(ban.getUnbansAt());
        assertEquals(testUserId, ban.getUserId());
    }


    @Test
    @DisplayName("유저 정지 기간 증가")
    public void extendBanDuration() {
        //given
        Ban ban = Ban.create(testUserId, testBanDuration);
        Long days = 3L;
        LocalDateTime initialUnbansAt = ban.getUnbansAt();

        //when
        ban.extendBanDuration(days); //3일 증가

        //then
        assertNotNull(ban);
        assertEquals(ban.getUnbansAt(), initialUnbansAt.plusDays(days));
    }
}

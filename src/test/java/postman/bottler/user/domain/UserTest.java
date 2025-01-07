package postman.bottler.user.domain;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("유저 도메인 테스트")
class UserTest {
    private String email;
    private String password;
    private String nickname;
    private String imageUrl;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        password = "password123";
        nickname = "tester";
        imageUrl = "http://example.com/image.jpg";
    }

    @Test
    @DisplayName("유저 객체를 생성한다.")
    public void createUser() {
        //when
        User user = User.createUser(1L, email, password, nickname, imageUrl,
                Role.USER, Provider.LOCAL, LocalDateTime.now(), LocalDateTime.now(),
                false, 0);

        //then
        assertNotNull(user);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getNickname(), nickname);
        assertEquals(user.getImageUrl(), imageUrl);
    }

    @Test
    @DisplayName("유저 객체를 생성한다. - 로컬 회원가입")
    public void createLocalUser() {
        //when
        User user = User.createUser(email, password, nickname, imageUrl);

        //then
        assertNotNull(user);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getNickname(), nickname);
        assertEquals(user.getImageUrl(), imageUrl);
        assertEquals(user.getRole(), Role.USER);
        assertEquals(user.getProvider(), Provider.LOCAL);
        assertEquals(user.getWarningCount(), 0);
    }

    @Test
    @DisplayName("유저 객체를 생성한다. - 카카오 유저 회원가입")
    public void createKakaoUser() {
        //when
        User user = User.createKakaoUser(email, nickname, imageUrl, password);

        //then
        assertNotNull(user);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getNickname(), nickname);
        assertEquals(user.getImageUrl(), imageUrl);
        assertEquals(user.getRole(), Role.USER);
        assertEquals(user.getProvider(), Provider.KAKAO);
        assertEquals(user.getWarningCount(), 0);
    }

    @Test
    @DisplayName("유저 객체를 생성한다. - 개발자 계정 회원가입")
    public void createDeveloperUser() {
        //when
        User user = User.createDeveloper(email, password, nickname, imageUrl);

        //then
        assertNotNull(user);
        assertEquals(user.getEmail(), email);
        assertEquals(user.getPassword(), password);
        assertEquals(user.getNickname(), nickname);
        assertEquals(user.getImageUrl(), imageUrl);
        assertEquals(user.getRole(), Role.DEVELOPER);
        assertEquals(user.getProvider(), Provider.LOCAL);
        assertEquals(user.getWarningCount(), 0);
    }

    @Test
    @DisplayName("경고 횟수를 1 증가한다.")
    public void updateWarningCount() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);
        LocalDateTime initialUpdatedAt = user.getUpdatedAt();

        //when
        user.updateWarningCount();

        //then
        assertEquals(user.getWarningCount(), 1);
        assertThat(user.getUpdatedAt()).isAfter(initialUpdatedAt);
    }

    @Test
    @DisplayName("경고 횟수가 최대 경고 횟수 이상일 경우, 경고 횟수를 0으로 수정하고 true를 반환한다.")
    public void checkBan() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);
        LocalDateTime initialUpdatedAt = user.getUpdatedAt();

        //when
        user.updateWarningCount();
        user.updateWarningCount();
        user.updateWarningCount();
        boolean result = user.checkBan();

        //then
        assertThat(user.getWarningCount()).isEqualTo(0);
        assertThat(result).isEqualTo(true);
        assertThat(user.getUpdatedAt()).isAfter(initialUpdatedAt);
    }

    @Test
    @DisplayName("경고 횟수가 최대 경고 횟수 미만일 경우, false를 반환한다.")
    public void checkUnBan() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);

        //when
        boolean result = user.checkBan();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("정지된 유저는 true를 반환한다.")
    public void isBanned() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);

        //when
        user.banned();
        boolean result = user.isBanned();

        //then
        assertThat(result).isEqualTo(true);
    }

    @Test
    @DisplayName("정지된 유저가 아니라면 false를 반환한다.")
    public void isUnBanned() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);

        //when
        user.unban();
        boolean result = user.isBanned();

        //then
        assertThat(result).isEqualTo(false);
    }

    @Test
    @DisplayName("유저의 role을 USER로 변경한다.")
    public void unban() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);

        //when
        user.unban();

        //then
        assertThat(user.getRole()).isEqualTo(Role.USER);
    }

    @Test
    @DisplayName("유저의 role을 BAN_USER로 변경한다.")
    public void banned() {
        //given
        User user = User.createUser(email, password, nickname, imageUrl);

        //when
        user.banned();

        //then
        assertThat(user.getRole()).isEqualTo(Role.BAN_USER);
    }
}

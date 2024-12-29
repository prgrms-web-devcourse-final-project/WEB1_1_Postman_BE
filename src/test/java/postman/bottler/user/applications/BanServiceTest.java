package postman.bottler.user.applications;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import postman.bottler.user.application.repository.BanRepository;
import postman.bottler.user.application.repository.UserRepository;
import postman.bottler.user.application.service.BanService;
import postman.bottler.user.domain.Ban;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.UserBanException;

@DisplayName("정지 서비스 테스트")
class BanServiceTest {
    @InjectMocks
    private BanService banService;

    @Mock
    private BanRepository banRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("정지된 유저가 아닌 유저를 정지시킨다.")
    public void banUser_whenUserIsNotBanned() {
        //given
        User user = mock(User.class);
        when(user.isBanned()).thenReturn(false);

        //when
        banService.banUser(user);

        //then
        verify(user).banned();
        verify(banRepository).save(any(Ban.class));
    }

    @Test
    @DisplayName("정지된 유저가 다시 정지되면 정지 기간을 늘린다.")
    public void banUser_whenUserIsBanned() {
        //given
        User user = mock(User.class);
        when(user.isBanned()).thenReturn(true);
        Ban userBan = mock(Ban.class);
        when(banRepository.findByUserId(user.getUserId())).thenReturn(Optional.of(userBan));

        //when
        banService.banUser(user);

        //then
        verify(userBan).extendBanDuration(7L);
        verify(banRepository).updateBan(userBan);
    }

    @Test
    @DisplayName("정지된 유저를 찾을 수 없는 경우, 예외 처리한다.")
    public void banUser_whenUserIsBanned_NotFound_throwException() {
        //given
        User user = mock(User.class);
        when(user.isBanned()).thenReturn(true);
        when(banRepository.findByUserId(user.getUserId())).thenReturn(Optional.empty());

        //when & then
        assertThatThrownBy(() -> banService.banUser(user))
                .isInstanceOf(UserBanException.class)
                .hasMessage("정지된 유저가 아닙니다.");
    }
}
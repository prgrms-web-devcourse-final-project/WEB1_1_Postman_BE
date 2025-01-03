package postman.bottler.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import postman.bottler.keyword.application.service.RedisLetterService;
import postman.bottler.letter.application.dto.LetterBoxDTO;
import postman.bottler.letter.application.service.LetterBoxService;
import postman.bottler.user.application.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.application.dto.response.SignInDTO;
import postman.bottler.user.application.repository.ProfileImageRepository;
import postman.bottler.user.application.repository.RefreshTokenRepository;
import postman.bottler.user.application.repository.UserRepository;
import postman.bottler.user.application.service.UserService;
import postman.bottler.user.auth.JwtTokenProvider;
import postman.bottler.user.domain.RefreshToken;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.TokenException;

@DisplayName("유저 서비스 테스트")
class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProfileImageRepository profileImageRepository;

    @Mock
    private RedisLetterService redisLetterService;

    @Mock
    private LetterBoxService letterBoxService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("회원가입을 성공한다.")
    public void createUser() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String nickname = "testNickname";
        String profileImageUrl = "testImage";
        List<Long> randomLetters = Arrays.asList(1L, 2L, 3L);

        when(profileImageRepository.findProfileImage()).thenReturn(profileImageUrl);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.createUser(
                    1L,
                    user.getEmail(),
                    user.getPassword(),
                    user.getNickname(),
                    user.getImageUrl(),
                    user.getRole(),
                    user.getProvider(),
                    user.getCreatedAt(),
                    user.getUpdatedAt(),
                    user.isDeleted(),
                    user.getWarningCount()
            );
        });

        //when
        userService.createUser(email, password, nickname);

        //then
        verify(profileImageRepository, times(1)).findProfileImage();
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
        verify(redisLetterService, times(1)).saveDeveloperLetter(eq(1L), anyList());
        verify(letterBoxService, times(3)).saveLetter(any(LetterBoxDTO.class));
    }

    @Test
    @DisplayName("개발자 계정을 생성한다. ")
    public void createDeveloper() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String nickname = "testNickname";

        //when
        userService.createDeveloper(email, password, nickname);

        //then
        verify(profileImageRepository, times(1)).findProfileImage();
        verify(passwordEncoder, times(1)).encode(password);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이메일 중복 체크 - 중복되지 않았을 경우")
    public void checkEmail_success() {
        //given
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        //when
        userService.checkEmail(email);

        //then
        verify(userRepository, times(1)).existsByEmail(email);
    }

    @Test
    @DisplayName("이메일 중복 체크 - 중복된 경우 EmailException 예외를 던진다.")
    public void checkEmail_exception() {
        //given
        String email = "test@test.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> userService.checkEmail(email))
                .isInstanceOf(EmailException.class)
                .hasMessage("이메일이 중복되었습니다.");
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복되지 않았을 경우")
    public void checkNickname_success() {
        //given
        String nickname  = "testNickname";
        when(userRepository.existsByNickname(nickname)).thenReturn(false);

        //when
        userService.checkNickname(nickname);

        //then
        verify(userRepository, times(1)).existsByNickname(nickname);
    }

    @Test
    @DisplayName("닉네임 중복 체크 - 중복된 경우 NicknameException 예외를 던진다.")
    public void checkNickname_exception() {
        //given
        String nickname = "testNickname";
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> userService.checkNickname(nickname))
                .isInstanceOf(NicknameException.class)
                .hasMessage("닉네임이 중복되었습니다.");
    }

    @Test
    @DisplayName("정상적으로 로그인하고 토큰을 반환한다.")
    public void signin_success() {
        //given
        String email = "test@test.com";
        String password = "testPassword";
        String accessToken = "testAccessToken";
        String refreshToken = "testRefreshToken";

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        Authentication authentication = mock(Authentication.class);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(authToken)).thenReturn(authentication);
        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn(accessToken);
        when(jwtTokenProvider.createRefreshToken(authentication)).thenReturn(refreshToken);

        doNothing().when(refreshTokenRepository)
                .createRefreshToken(any(RefreshToken.class));

        //when
        SignInDTO result = userService.signin(email, password);

        //then
        assertThat(result).isNotNull();
        assertThat(result.accessToken()).isEqualTo(accessToken);
        assertThat(result.refreshToken()).isEqualTo(refreshToken);

        verify(authenticationManager).authenticate(authToken);
        verify(jwtTokenProvider).createAccessToken(authentication);
        verify(jwtTokenProvider).createRefreshToken(authentication);
        verify(refreshTokenRepository).createRefreshToken(any(RefreshToken.class));
    }

    @Test
    @DisplayName("비밀번호가 틀릴 경우 PasswordException 예외를 던진다.")
    public void signin_exception() {
        //given
        String email = "test@test.com";
        String password = "testPassword";

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(email, password);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        doThrow(new BadCredentialsException("Bad credentials"))
                .when(authenticationManager).authenticate(authToken);

        //when & then
        assertThatThrownBy(() -> userService.signin(email, password))
                .isInstanceOf(PasswordException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");

        verify(authenticationManager).authenticate(authToken);
        verifyNoInteractions(jwtTokenProvider, refreshTokenRepository);
    }

    @Test
    @DisplayName("리프레시 토큰이 유효한 경우 새로운 액세스 토큰을 반환한다.")
    public void validateRefreshToken_success() {
        //given
        String refreshToken = "testRefreshToken";
        String emailFromRefreshToken = "test@test.com";
        String storedEmail = "test@test.com";
        String newAccessToken = "newAccessToken";

        when(jwtTokenProvider.getEmailFromToken(refreshToken)).thenReturn(emailFromRefreshToken);
        when(refreshTokenRepository.findEmailByRefreshToken(refreshToken)).thenReturn(storedEmail);

        Authentication authentication = mock(Authentication.class);
        when(jwtTokenProvider.getAuthentication(refreshToken)).thenReturn(authentication);
        when(jwtTokenProvider.createAccessToken(authentication)).thenReturn(newAccessToken);

        //when
        AccessTokenResponseDTO result = userService.validateRefreshToken(refreshToken);

        //then
        assertThat(result).isNotNull();
        assertThat(result.newAccessToken()).isEqualTo(newAccessToken);

        verify(jwtTokenProvider).getEmailFromToken(refreshToken);
        verify(refreshTokenRepository).findEmailByRefreshToken(refreshToken);
        verify(jwtTokenProvider).getAuthentication(refreshToken);
        verify(jwtTokenProvider).createAccessToken(authentication);
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하지 않은 경우 TokenException 예외를 던진다.")
    public void validateRefreshToken_exception() {
        //given
        String refreshToken = "testRefreshToken";
        String emailFromRefreshToken = "test@test.com";
        String storedEmail = "test@test.com";

        when(jwtTokenProvider.getEmailFromToken(refreshToken)).thenReturn(emailFromRefreshToken);
        when(refreshTokenRepository.findEmailByRefreshToken(refreshToken)).thenReturn(storedEmail);

        //when & then
        assertThatThrownBy(() -> userService.validateRefreshToken(refreshToken))
                .isInstanceOf(TokenException.class)
                .hasMessage("유효하지 않은 jwt 토큰입니다.");

        verify(jwtTokenProvider).getEmailFromToken(refreshToken);
        verify(refreshTokenRepository).findEmailByRefreshToken(refreshToken);
        verifyNoMoreInteractions(jwtTokenProvider, refreshTokenRepository);
    }

    @Test
    @DisplayName("비밀번호가 일치하면 유저를 삭제하고, 리프레시 토큰도 삭제한다.")
    public void deleteUser_success() {
        //given
        

        //when

        //then
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 유저는 삭제되지 않고, PasswordException 예외를 던진다.")
    public void deleteUser_exception() {
        //given

        //when

        //then
    }
}
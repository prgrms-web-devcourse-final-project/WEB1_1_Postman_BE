package postman.bottler.user.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import postman.bottler.notification.application.service.NotificationService;
import postman.bottler.notification.domain.NotificationType;
import postman.bottler.slack.SlackConstant;
import postman.bottler.slack.SlackService;
import postman.bottler.user.application.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.application.dto.response.ExistingUserResponseDTO;
import postman.bottler.user.application.dto.response.SignInDTO;
import postman.bottler.user.application.dto.response.UserResponseDTO;
import postman.bottler.user.application.repository.EmailCodeRepository;
import postman.bottler.user.application.repository.ProfileImageRepository;
import postman.bottler.user.application.repository.RefreshTokenRepository;
import postman.bottler.user.application.repository.UserRepository;
import postman.bottler.user.application.service.BanService;
import postman.bottler.user.application.service.EmailService;
import postman.bottler.user.application.service.UserService;
import postman.bottler.user.auth.JwtTokenProvider;
import postman.bottler.user.domain.EmailCode;
import postman.bottler.user.domain.EmailForm;
import postman.bottler.user.domain.ProfileImage;
import postman.bottler.user.domain.Provider;
import postman.bottler.user.domain.RefreshToken;
import postman.bottler.user.domain.Role;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.ProfileImageException;
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

    @Mock
    private EmailService emailService;

    @Mock
    private EmailCodeRepository emailCodeRepository;

    @Mock
    private SlackService slackService;

    @Mock
    private BanService banService;

    @Mock
    private NotificationService notificationService;

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
        String nickname = "testNickname";
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
        String storedEmail = "stored@test.com";

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
        String password = "testPassword";
        String email = "testEmail";
        String encodedPassword = "testEncodedPassword";

        User user = User.createUser(
                1L,
                email,
                encodedPassword,
                "testNickname",
                "testImageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);

        //when
        userService.deleteUser(password, email);

        //then
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, encodedPassword);
        verify(userRepository).softDeleteUser(user.getUserId());
        verify(refreshTokenRepository).deleteByEmail(email);
        verifyNoMoreInteractions(userRepository, passwordEncoder, refreshTokenRepository);
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 유저는 삭제되지 않고, PasswordException 예외를 던진다.")
    public void deleteUser_exception() {
        String email = "test@test.com";
        String password = "wrongPassword";
        String storedPassword = "encodedPassword";

        User user = User.createUser(
                1L,
                email,
                storedPassword,
                "nickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(password, storedPassword)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> userService.deleteUser(password, email))
                .isInstanceOf(PasswordException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(password, storedPassword);
        verifyNoMoreInteractions(userRepository, passwordEncoder, refreshTokenRepository);
    }

    @Test
    @DisplayName("이메일로 해당 유저를 찾는다.")
    public void findUser_success() {
        //given
        String email = "test@test.com";

        User user = User.createUser(
                1L,
                email,
                "encodedPassword",
                "nickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        UserResponseDTO expectedResponse = UserResponseDTO.from(user);
        when(userRepository.findByEmail(email)).thenReturn(user);

        //when
        UserResponseDTO actualResponse = userService.findUser(email);

        //then
        assertThat(actualResponse).isNotNull();
        assertThat(actualResponse).usingRecursiveComparison().isEqualTo(expectedResponse);
        verify(userRepository).findByEmail(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("닉네임 중복 확인 후 닉네임을 수정한다.")
    public void updateNickname_success() {
        //given
        String email = "test@test.com";
        String newNickname = "newNickname";

        User user = User.createUser(
                1L,
                email,
                "encodedPassword",
                "oldNickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.existsByNickname(newNickname)).thenReturn(false);
        when(userRepository.findByEmail(email)).thenReturn(user);

        //when
        userService.updateNickname(newNickname, email);

        //then
        verify(userRepository).existsByNickname(newNickname);
        verify(userRepository).findByEmail(email);
        verify(userRepository).updateNickname(user.getUserId(), newNickname);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("닉네임이 중복된 경우 NicknameException 예외를 던진다.")
    public void updateNickname_exception() {
        //given
        String email = "test@test.com";
        String newNickname = "duplicateNickname";

        when(userRepository.existsByNickname(newNickname)).thenReturn(true);

        //when & then
        assertThatThrownBy(() -> userService.updateNickname(newNickname, email))
                .isInstanceOf(NicknameException.class)
                .hasMessage("닉네임이 중복되었습니다.");

        verify(userRepository).existsByNickname(newNickname);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("기존 비밀번호 확인 후 새로운 비밀번호로 수정한다.")
    public void updatePassword_success() {
        String email = "test@test.com";
        String existingPassword = "correctPassword";
        String newPassword = "newPassword";
        String encodedExistingPassword = "encodedExistingPassword";
        String encodedNewPassword = "encodedNewPassword";

        User user = User.createUser(
                1L,
                email,
                encodedExistingPassword,
                "nickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(existingPassword, encodedExistingPassword)).thenReturn(true);
        when(passwordEncoder.encode(newPassword)).thenReturn(encodedNewPassword);

        //when
        userService.updatePassword(existingPassword, newPassword, email);

        //then
        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(existingPassword, encodedExistingPassword);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).updatePassword(user.getUserId(), encodedNewPassword);
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("기존 비밀번호가 일치하지 않는 경우 PasswordException 예외를 던진다.")
    public void updatePassword_exception() {
        //given
        String email = "test@test.com";
        String existingPassword = "wrongPassword";
        String encodedExistingPassword = "encodedExistingPassword";

        User user = User.createUser(
                1L,
                email,
                encodedExistingPassword,
                "nickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.findByEmail(email)).thenReturn(user);
        when(passwordEncoder.matches(existingPassword, encodedExistingPassword)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> userService.updatePassword(existingPassword, "newPassword", email))
                .isInstanceOf(PasswordException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");

        verify(userRepository).findByEmail(email);
        verify(passwordEncoder).matches(existingPassword, encodedExistingPassword);
        verifyNoMoreInteractions(userRepository, passwordEncoder);
    }

    @Test
    @DisplayName("새로운 프로필 이미지 url이 유효한 경우 email에 해당하는 유저의 프로필 이미지를 변경한다.")
    public void updateProfileImage_success() {
        //given
        String email = "test@test.com";
        String newProfileImage = "http://example.com/1.jpg";

        User user = User.createUser(
                1L,
                email,
                "encodedPassword",
                "nickname",
                "oldImageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(profileImageRepository.existsByUrl(newProfileImage)).thenReturn(true);
        when(userRepository.findByEmail(email)).thenReturn(user);

        //when
        userService.updateProfileImage(newProfileImage, email);

        //then
        verify(profileImageRepository).existsByUrl(newProfileImage);
        verify(userRepository).findByEmail(email);
        verify(userRepository).updateProfileImageUrl(user.getUserId(), newProfileImage);
        verifyNoMoreInteractions(profileImageRepository, userRepository);
    }

    @Test
    @DisplayName("새로운 프로필 이미지 url이 유효하지 않은 경우 ProfileImageException 예외를 던진다.")
    public void updateProfileImage_exception() {
        //given
        String email = "test@test.com";
        String newProfileImage = "http://example.com/1.jpg";

        when(profileImageRepository.existsByUrl(newProfileImage)).thenReturn(false);

        //when & then
        assertThatThrownBy(() -> userService.updateProfileImage(newProfileImage, email))
                .isInstanceOf(ProfileImageException.class)
                .hasMessage("유효하지 않은 프로필 이미지 URL입니다.");

        verify(profileImageRepository).existsByUrl(newProfileImage);
        verifyNoMoreInteractions(profileImageRepository, userRepository);
    }

    @Test
    @DisplayName("입력된 프로필 이미지 url을 저장한다.")
    public void createProfileImg() {
        //given
        String profileImageUrl = "http://example.com/1.jpg";

        //when
        userService.createProfileImg(profileImageUrl);

        //then
        ArgumentCaptor<ProfileImage> profileImageCaptor = ArgumentCaptor.forClass(ProfileImage.class);
        verify(profileImageRepository).save(profileImageCaptor.capture());

        ProfileImage savedProfileImage = profileImageCaptor.getValue();
        assertThat(savedProfileImage.getImageUrl()).isEqualTo(profileImageUrl);
        verifyNoMoreInteractions(profileImageRepository);
    }

    @Test
    @DisplayName("nickname에 해당하는 유저가 존재하는지 확인한다.")
    public void findExistingUser() {
        //given
        String nickname = "testNickname";
        when(userRepository.existsByNickname(nickname)).thenReturn(true);

        //when
        ExistingUserResponseDTO response = userService.findExistingUser(nickname);

        //then
        assertThat(response.isExists()).isEqualTo(true);
        verify(userRepository).existsByNickname(nickname);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    @DisplayName("주어진 email로 인증 코드를 정상적으로 보낸다.")
    public void sendCodeToEmail_success() throws MessagingException {
        //given
        String email = "test@test.com";
        String authCode = "testCode";
        EmailForm emailForm = EmailForm.EMAIL_AUTH;
        String title = emailForm.getTitle();
        String content = emailForm.getContent(authCode);

        doNothing().when(emailService).sendEmail(email, title, content);
        doNothing().when(emailCodeRepository).save(any(EmailCode.class));

        //when
        userService.sendCodeToEmail(email);

        //then
        verify(emailService).sendEmail(eq(email), eq(title), any());
        verify(emailCodeRepository).save(any(EmailCode.class));
    }

    @Test
    @DisplayName("유효한 이메일과 코드에 대해 인증을 성공적으로 확인한다.")
    public void verifyCode() {
        //given
        String email = "test@test.com";
        String code = "testCode";

        EmailCode emailCode = mock(EmailCode.class);
        when(emailCodeRepository.findEmailCode(email, code)).thenReturn(emailCode);
        doNothing().when(emailCode).checkExpiration();

        //when
        userService.verifyCode(email, code);

        //then
        verify(emailCodeRepository).findEmailCode(email, code);
        verify(emailCode).checkExpiration();
    }

    @Test
    @DisplayName("주어진 userId로 유저를 조회한다.")
    public void findById() {
        //given
        Long userId = 1L;

        User user = User.createUser(
                userId,
                "email",
                "password",
                "nickname",
                "imageUrl",
                Role.USER,
                Provider.LOCAL,
                LocalDateTime.now(),
                LocalDateTime.now(),
                false,
                0
        );

        when(userRepository.findById(userId)).thenReturn(user);

        //when
        User resultUser = userService.findById(userId);

        // then
        assertThat(user).isEqualTo(resultUser);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("카카오 로그인 시 새로운 사용자가 등록되고 토큰이 반환된다.")
    public void kakaoSignin_success_newUser() {
        //given
        String kakaoId = "kakao123";
        String nickname = "testNickname";
        String profileImageUrl = "http://image.url";

        when(userRepository.existsByEmailAndProvider(kakaoId)).thenReturn(false);
        when(profileImageRepository.findProfileImage()).thenReturn(profileImageUrl);

        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(Authentication.class));
        when(jwtTokenProvider.createAccessToken(any(Authentication.class))).thenReturn("testAccessToken");
        when(jwtTokenProvider.createRefreshToken(any(Authentication.class))).thenReturn("testRefreshToken");

        //when
        SignInDTO signInDTO = userService.kakaoSignin(kakaoId, nickname);

        //then
        verify(userRepository).existsByEmailAndProvider(kakaoId);
        verify(userRepository).save(any());
        assertThat(signInDTO.accessToken()).isEqualTo("testAccessToken");
        assertThat(signInDTO.refreshToken()).isEqualTo("testRefreshToken");
    }

    @Test
    @DisplayName("userId에 해당하는 유저의 프로필 이미지를 조회한다.")
    public void getProfileImageUrlById() {
        //given
        Long userId = 1L;
        String expectedImageUrl = "http://image.url";

        User mockUser = mock(User.class);
        when(mockUser.getImageUrl()).thenReturn(expectedImageUrl);
        when(userRepository.findById(userId)).thenReturn(mockUser);

        //when
        String actualImageUrl = userService.getProfileImageUrlById(userId);

        //then
        assertThat(expectedImageUrl).isEqualTo(actualImageUrl);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("userId에 해당하는 유저의 닉네임을 조회한다.")
    public void getNicknameById() {
        //given
        Long userId = 1L;
        String expectedNickname = "nickname";

        User mockUser = mock(User.class);
        when(mockUser.getNickname()).thenReturn(expectedNickname);
        when(userRepository.findById(userId)).thenReturn(mockUser);

        //when
        String actualNickname = userService.getNicknameById(userId);

        //then
        assertThat(expectedNickname).isEqualTo(actualNickname);
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("userId에 해당하는 유저의 경고 횟수를 증가하고 슬랙 메시지를 보낸다.")
    public void updateWarningCount() {
        //given
        Long userId = 1L;
        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(user);
        when(user.checkBan()).thenReturn(false);

        //when
        userService.updateWarningCount(userId);

        //then
        verify(userRepository).findById(userId);
        verify(user).updateWarningCount();
        verify(slackService).sendSlackMessage(SlackConstant.WARNING, userId);
        verify(userRepository).updateWarningCount(user);
        verifyNoInteractions(banService, notificationService);
    }

    @Test
    @DisplayName("userId에 해당하는 유저의 증가한 경고 횟수가 최대 경고 횟수 이상인 경우 유저를 정지시킨 후 슬랙 메시지를 보낸다.")
    public void updateWarningCount_banUser() {
        //given
        Long userId = 1L;
        User user = mock(User.class);

        when(userRepository.findById(userId)).thenReturn(user);
        when(user.checkBan()).thenReturn(true);

        //when
        userService.updateWarningCount(userId);

        //then
        verify(userRepository).findById(userId);
        verify(user).updateWarningCount();
        verify(slackService).sendSlackMessage(SlackConstant.WARNING, userId);
        verify(user).checkBan();
        verify(banService).banUser(user);
        verify(slackService).sendSlackMessage(SlackConstant.BAN, userId);
        verify(notificationService).sendNotification(NotificationType.BAN, userId, null, null);
        verify(userRepository).updateWarningCount(user);
    }

    @Test
    @DisplayName("전체 유저의 아이디를 조회한다.")
    public void getAllUserIds() {
        //given
        LocalDateTime now = LocalDateTime.now();
        List<User> mockUsers = List.of(
                User.createUser(1L, "user1@example.com", "password1", "nickname1", "profileImageUrl1", Role.USER,
                        Provider.KAKAO, now, now, false, 0),
                User.createUser(2L, "user2@example.com", "password2", "nickname2", "profileImageUrl2", Role.USER,
                        Provider.KAKAO, now, now, false, 0)
        );
        when(userRepository.findAllUserId()).thenReturn(mockUsers);

        //when
        List<Long> result = userService.getAllUserIds();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(1L, 2L);
        verify(userRepository).findAllUserId();
    }

    @Test
    @DisplayName("주어진 nickname에 해당하는 유저의 userId를 조회한다.")
    public void getUserIdByNickname() {
        //given
        String nickname = "nickname";
        LocalDateTime now = LocalDateTime.now();
        User mockUser = User.createUser(1L, "user1@example.com", "password1", nickname, "profileImageUrl1", Role.USER,
                Provider.KAKAO, now, now, false, 0);
        when(userRepository.findByNickname(nickname)).thenReturn(mockUser);

        //when
        Long result = userService.getUserIdByNickname(nickname);

        //then
        assertThat(result).isEqualTo(1L);
        verify(userRepository).findByNickname(nickname);
    }

    @Test
    @DisplayName("주어진 email에 해당하는 이메일 인증 코드를 삭제한다.")
    public void deleteEmailCode() {
        //given
        String email = "test@test.com";
        doNothing().when(emailCodeRepository).deleteByEmail(email);

        //when
        userService.deleteEmailCode(email);

        //then
        verify(emailCodeRepository).deleteByEmail(email);
    }
}

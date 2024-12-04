package postman.bottler.user.service;

import jakarta.mail.MessagingException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.user.auth.JwtTokenProvider;
import postman.bottler.user.domain.EmailCode;
import postman.bottler.user.domain.EmailForm;
import postman.bottler.user.domain.ProfileImage;
import postman.bottler.user.domain.RefreshToken;
import postman.bottler.user.domain.User;
import postman.bottler.user.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.dto.response.ExistingUserResponseDTO;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.dto.response.UserResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.ProfileImageException;
import postman.bottler.user.exception.TokenException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ProfileImageRepository profileImageRepository;
    private final EmailCodeRepository emailCodeRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final EmailService emailService;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int CODE_LENGTH = 8;
    private static final SecureRandom random = new SecureRandom();

    @Transactional
    public void createUser(String email, String password, String nickname) {
        String profileImageUrl = profileImageRepository.findProfileImage();
        User user = User.createUser(email, passwordEncoder.encode(password), nickname, profileImageUrl);
        userRepository.save(user);
    }

    @Transactional
    public void checkEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailException("이메일이 중복되었습니다.");
        }
    }

    @Transactional
    public void checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new NicknameException("닉네임이 중복되었습니다.");
        }
    }

    @Transactional
    public SignInResponseDTO signin(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String accessToken = jwtTokenProvider.createToken(authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

            refreshTokenRepository.createRefreshToken(RefreshToken.createRefreshToken(email, refreshToken));

            return new SignInResponseDTO(accessToken, refreshToken);
        } catch (BadCredentialsException e) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
    }

    @Transactional
    public AccessTokenResponseDTO validateRefreshToken(String refreshToken) {
        //db에 저장되어 있는 email과 refreshToken의 email과 같은지 비교
        String emailFromRefreshToken = jwtTokenProvider.getEmailFromToken(refreshToken);
        String storedEmail = refreshTokenRepository.findEmailByRefreshToken(refreshToken);
        if (!storedEmail.equals(emailFromRefreshToken)) {
            throw new TokenException("유효하지 않은 jwt 토큰입니다.");
        }

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
        String newAccessToken = jwtTokenProvider.createToken(authentication);
        return new AccessTokenResponseDTO(newAccessToken);
    }

    @Transactional
    public void deleteUser(String password, String email) {
        User user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.softDeleteUser(user.getUserId());
        refreshTokenRepository.deleteByEmail(email);
    }

    @Transactional
    public UserResponseDTO findUser(String email) {
        User user = userRepository.findByEmail(email);
        return UserResponseDTO.from(user);
    }

    @Transactional
    public void updateNickname(String nickname, String email) {
        if (userRepository.existsByNickname(nickname)) {
            throw new NicknameException("닉네임이 중복되었습니다.");
        }
        User user = userRepository.findByEmail(email);
        userRepository.updateNickname(user.getUserId(), nickname);
    }

    @Transactional
    public void updatePassword(String existingPassword, String newPassword, String email) {
        User user = userRepository.findByEmail(email);
        if (!passwordEncoder.matches(existingPassword, user.getPassword())) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
        userRepository.updatePassword(user.getUserId(), passwordEncoder.encode(newPassword));
    }

    @Transactional
    public void updateProfileImage(String newProfileImage, String email) {
        if (!profileImageRepository.existsByUrl(newProfileImage)) {
            throw new ProfileImageException("유효하지 않은 프로필 이미지 URL입니다.");
        }
        User user = userRepository.findByEmail(email);
        userRepository.updateProfileImageUrl(user.getUserId(), newProfileImage);
    }

    @Transactional
    public void createProfileImg(String profileImageUrl) {
        ProfileImage profileImage = ProfileImage.createProfileImg(profileImageUrl);
        profileImageRepository.save(profileImage);
    }

    @Transactional
    public ExistingUserResponseDTO findExistingUser(String nickname) {
        return new ExistingUserResponseDTO(userRepository.existsByNickname(nickname));
    }

    @Transactional
    public void sendCodeToEmail(String email) {
        String authCode = createCode();
        EmailForm emailForm = EmailForm.EMAIL_AUTH;

        String title = emailForm.getTitle();
        String content = emailForm.getContent(authCode);

        try {
            emailService.sendEmail(email, title, content);
            emailCodeRepository.save(EmailCode.createEmailCode(email, authCode));
        } catch (RuntimeException | MessagingException e) {
            throw new EmailException("인증코드 요청에 실패했습니다.");
        }
    }

    private String createCode() {
        StringBuilder code = new StringBuilder(CODE_LENGTH);
        for (int i = 0; i < CODE_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(index));
        }
        return code.toString();
    }

    @Transactional
    public void verifyCode(String email, String code) {
        EmailCode emailCode = emailCodeRepository.findEmailCode(email, code);
        emailCode.checkExpiration();
    }

    @Transactional
    public User findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public SignInResponseDTO kakaoSignin(String kakaoId, String nickname) {
        if (!userRepository.existsByEmailAndProvider(kakaoId)) {
            nickname = generateUniqueNickname(nickname);
            String profileImageUrl = profileImageRepository.findProfileImage();
            User user = User.createKakaoUser(kakaoId, nickname, profileImageUrl, passwordEncoder.encode(kakaoId));
            userRepository.save(user);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(kakaoId, kakaoId);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtTokenProvider.createToken(authentication);
        String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

        refreshTokenRepository.createRefreshToken(RefreshToken.createRefreshToken(kakaoId, refreshToken));

        return new SignInResponseDTO(accessToken, refreshToken);
    }

    private String generateUniqueNickname(String nickname) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);

        while (userRepository.existsByNickname(nickname)) {
            int randomNumber = random.nextInt(10000);
            nickname = nickname + randomNumber;
        }
        return nickname;
    }

    //아이디로 프로필 이미지 조회
    public String getProfileImageUrlById(Long userId) {
        return userRepository.findById(userId).getImageUrl();
    }

    //아이디로 닉네임 조회
    public String getNicknameById(Long userId) {
        return userRepository.findById(userId).getNickname();
    }

    //유저 경고 횟수 증가
    public void updateWarningCount(Long userId) {
        User user = userRepository.findById(userId);
        user.updateWarningCount();
    }

    //전체 유저 아이디 조회
    public List<Long> getAllUserId() {
        List<User> users = userRepository.findAllUserId();
        return users.stream()
                .map(User::getUserId)
                .collect(Collectors.toList());
    }
}

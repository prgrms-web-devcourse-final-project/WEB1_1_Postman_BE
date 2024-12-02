package postman.bottler.user.service;

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
import postman.bottler.user.domain.RefreshToken;
import postman.bottler.user.domain.User;
import postman.bottler.user.dto.response.AccessTokenResponseDTO;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.dto.response.UserResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;
import postman.bottler.user.exception.TokenException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void createUser(String email, String password, String nickname) {
        User user = User.createUser(email, passwordEncoder.encode(password), nickname);
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
    }

    @Transactional
    public UserResponseDTO findUser(String email) {
        User user = userRepository.findByEmail(email);
        return UserResponseDTO.from(user);
    }

    @Transactional
    public void updateNickname(String nickname, String email) {
        User user = userRepository.findByEmail(email);
        userRepository.updateNickname(user.getUserId(), nickname);
    }
}

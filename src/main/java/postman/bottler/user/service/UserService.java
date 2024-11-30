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
import postman.bottler.user.domain.User;
import postman.bottler.user.dto.response.SignInResponseDTO;
import postman.bottler.user.exception.EmailException;
import postman.bottler.user.exception.NicknameException;
import postman.bottler.user.exception.PasswordException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Transactional
    public void createUser(String email, String password, String nickname) {
        User user = User.createUser(email, passwordEncoder.encode(password), nickname);
        userRepository.save(user);
    }

    public void checkEmail(String email) {
        if (userRepository.findUserByEmail(email)) {
            throw new EmailException("이메일이 중복되었습니다.");
        }
    }

    public void checkNickname(String nickname) {
        if (userRepository.findUserByNickname(nickname)) {
            throw new NicknameException("닉네임이 중복되었습니다.");
        }
    }

    public SignInResponseDTO signin(String email, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String accessToken = jwtTokenProvider.createToken(authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

            // TODO: refreshToken 저장

            return new SignInResponseDTO(accessToken, refreshToken);

        } catch (BadCredentialsException e) {
            throw new PasswordException("비밀번호가 일치하지 않습니다.");
        }
    }
}

package postman.bottler.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import postman.bottler.user.domain.User;
import postman.bottler.user.exception.EmailException;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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
}

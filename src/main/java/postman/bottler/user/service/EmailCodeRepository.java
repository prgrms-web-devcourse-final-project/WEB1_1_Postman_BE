package postman.bottler.user.service;

import postman.bottler.user.domain.EmailCode;

public interface EmailCodeRepository {
    void save(EmailCode emailCode);
    EmailCode findEmailCode(String email, String code);
}

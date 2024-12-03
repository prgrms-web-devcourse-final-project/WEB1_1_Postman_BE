package postman.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class EmailCode {
    private String email;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}

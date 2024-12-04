package postman.bottler.user.infra.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.user.domain.EmailCode;

@Entity
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "email_code")
public class EmailCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long emailCodeId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    public static EmailCodeEntity from(EmailCode emailCode) {
        return EmailCodeEntity.builder()
                .email(emailCode.getEmail())
                .code(emailCode.getCode())
                .createdAt(emailCode.getCreatedAt())
                .expiresAt(emailCode.getExpiresAt())
                .build();
    }

    public EmailCode to() {
        return EmailCode.createEmailCode(this.email, this.code, this.createdAt, this.expiresAt);
    }

    public static EmailCode toEmailCode(EmailCodeEntity emailCodeEntity) {
        return emailCodeEntity.to();
    }

    public void changeCode(String code) {
        this.code = code;
    }
}

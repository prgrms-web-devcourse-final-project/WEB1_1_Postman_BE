package postman.bottler.user.infra.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import postman.bottler.user.domain.Ban;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long banId;

    private Long userId;

    private LocalDateTime bannedAt;

    private LocalDateTime unbansAt;

    public static BanEntity from(Ban ban) {
        return BanEntity.builder()
                .userId(ban.getUserId())
                .bannedAt(ban.getBannedAt())
                .unbansAt(ban.getUnbansAt())
                .build();
    }

    public Ban toDomain() {
        return Ban.builder()
                .userId(userId)
                .bannedAt(bannedAt)
                .unbansAt(unbansAt)
                .build();
    }
}

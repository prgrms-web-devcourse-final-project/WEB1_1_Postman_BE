package postman.bottler.user.domain;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Ban {
    private final Long id;

    private final Long userId;

    private final LocalDateTime bannedAt;

    private LocalDateTime unbansAt;

    public static Ban create(Long userId, Long banDuration) {
        return Ban.builder()
                .userId(userId)
                .bannedAt(LocalDateTime.now())
                .unbansAt(LocalDateTime.now().plusDays(banDuration))
                .build();
    }

    public void extendBanDuration(Long days) {
        this.unbansAt = this.unbansAt.plusDays(days);
    }
}
